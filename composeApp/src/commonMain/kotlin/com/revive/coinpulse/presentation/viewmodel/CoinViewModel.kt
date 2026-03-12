package com.revive.coinpulse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revive.coinpulse.data.AppSettings
import com.revive.coinpulse.data.AppTheme
import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.data.model.PricePoint
import com.revive.coinpulse.domain.usecase.GetCachedCoinsUseCase
import com.revive.coinpulse.domain.usecase.GetCoinsUseCase
import com.revive.coinpulse.domain.usecase.GetMarketChartUseCase
import com.revive.coinpulse.domain.usecase.ObserveFavoritesUseCase
import com.revive.coinpulse.domain.usecase.ToggleFavoriteUseCase
import com.revive.coinpulse.getCurrentTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CoinUiState(
    val isLoading: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val errorMessage: String? = null,
    val lastUpdated: String = "",
    val isRefreshEnabled: Boolean = true,
    val favorites: Set<String> = emptySet(),
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val snackbarMessage: String? = null,
    val currency: String = "usd",
    val chartData: List<PricePoint> = emptyList(),
    val isChartLoading: Boolean = false,
    val chartError: String? = null
) {
    val filteredCoins: List<Coin>
        get() = if (searchQuery.isEmpty()) coins
        else coins.filter { it.name.contains(searchQuery, ignoreCase = true) }
}

data class SettingsUiState(
    val currency: String = "usd",
    val refreshInterval: Long = 60L,
    val coinCount: Int = 100,
    val theme: AppTheme = AppTheme.SYSTEM
)

class CoinViewModel(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val getCachedCoinsUseCase: GetCachedCoinsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val getMarketChartUseCase: GetMarketChartUseCase,
    private val appSettings: AppSettings
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoinUiState())
    val uiState: StateFlow<CoinUiState> = _uiState.asStateFlow()

    private val _settingsUiState = MutableStateFlow(
        SettingsUiState(
            currency = appSettings.currency,
            refreshInterval = appSettings.refreshInterval,
            coinCount = appSettings.coinCount,
            theme = appSettings.theme
        )
    )
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    private var pollingJob: Job? = null
    private var cooldownJob: Job? = null

    init {
        loadCache()
        startPolling()
        observeFavorites()
    }

    private fun loadCache() {
        if (getCachedCoinsUseCase.hasCachedData()) {
            _uiState.value = _uiState.value.copy(
                coins = getCachedCoinsUseCase(),
                lastUpdated = getCachedCoinsUseCase.getCacheTime()
            )
        }
    }

    private fun startPolling() {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (true) {
                getCoins()
                delay(appSettings.refreshInterval * 1000)
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            observeFavoritesUseCase().collect { favorites ->
                _uiState.value = _uiState.value.copy(favorites = favorites)
            }
        }
    }

    private suspend fun getCoins() {
        _uiState.value = _uiState.value.copy(
            isLoading = _uiState.value.coins.isEmpty(),
            errorMessage = null
        )

        getCoinsUseCase(
            currency = appSettings.currency,
            perPage = appSettings.coinCount
        ).fold(
            onSuccess = { coins ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    coins = coins,
                    lastUpdated = getCurrentTime(),
                    currency = appSettings.currency
                )
            },
            onFailure = { error ->
                val hasCachedData = _uiState.value.coins.isNotEmpty()
                if (hasCachedData) {
                    val message = when {
                        error.message?.contains("429") == true -> "Rate limit exceeded. Showing cached data."
                        else -> "Network error. Showing cached data."
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        snackbarMessage = message
                    )
                } else {
                    val message = when {
                        error.message?.contains("429") == true -> "Rate limit exceeded. Please wait a moment."
                        else -> error.message ?: "Unknown error"
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = message
                    )
                }
            }
        )
    }

    fun refresh() {
        if (!_uiState.value.isRefreshEnabled) return
        _uiState.value = _uiState.value.copy(isRefreshEnabled = false)

        cooldownJob?.cancel()
        cooldownJob = viewModelScope.launch {
            startPolling()
            delay(60_000)
            _uiState.value = _uiState.value.copy(isRefreshEnabled = true)
        }
    }

    fun toggleFavorite(coinId: String) = toggleFavoriteUseCase(coinId)

    fun loadChart(coinId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isChartLoading = true, chartError = null) }
            getMarketChartUseCase(coinId, _uiState.value.currency)
                .onSuccess { points ->
                    _uiState.update { it.copy(
                        chartData = points,
                        isChartLoading = false
                    )}
                }
                .onFailure { e ->
                    _uiState.update { it.copy(
                        isChartLoading = false,
                        chartError = e.message
                    )}
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onSearchActiveChange(isActive: Boolean) {
        _uiState.value = _uiState.value.copy(
            isSearchActive = isActive,
            searchQuery = if (!isActive) "" else _uiState.value.searchQuery
        )
    }

    fun onSnackbarDismissed() {
        _uiState.value = _uiState.value.copy(snackbarMessage = null)
    }

    fun onCurrencyChange(currency: String) {
        appSettings.currency = currency
        _settingsUiState.value = _settingsUiState.value.copy(currency = currency)
        _uiState.value = _uiState.value.copy(currency = currency, coins = emptyList())
        getCachedCoinsUseCase.clearCache()
        startPolling()
    }

    fun onRefreshIntervalChange(interval: Long) {
        appSettings.refreshInterval = interval
        _settingsUiState.value = _settingsUiState.value.copy(refreshInterval = interval)
        startPolling()
    }

    fun onCoinCountChange(count: Int) {
        appSettings.coinCount = count
        _settingsUiState.value = _settingsUiState.value.copy(coinCount = count)
        startPolling()
    }

    fun onThemeChange(theme: AppTheme) {
        appSettings.theme = theme
        _settingsUiState.value = _settingsUiState.value.copy(theme = theme)
    }

    override fun onCleared() {
        super.onCleared()
        pollingJob?.cancel()
        cooldownJob?.cancel()
    }
}