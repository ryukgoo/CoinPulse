package com.revive.coinpulse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.domain.usecase.GetCachedCoinsUseCase
import com.revive.coinpulse.domain.usecase.GetCoinsUseCase
import com.revive.coinpulse.domain.usecase.ObserveFavoritesUseCase
import com.revive.coinpulse.domain.usecase.ToggleFavoriteUseCase
import com.revive.coinpulse.getCurrentTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CoinUiState(
    val isLoading: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val errorMessage: String? = null,
    val lastUpdated: String = "",
    val isRefreshEnabled: Boolean = true,
    val favorites: Set<String> = emptySet(),
    val searchQuery: String = "",
    val isSearchActive: Boolean = false
) {
    val filteredCoins: List<Coin>
        get() = if (searchQuery.isEmpty()) coins
        else coins.filter { it.name.contains(searchQuery, ignoreCase = true) }
}

class CoinViewModel(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val getCachedCoinsUseCase: GetCachedCoinsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoinUiState())
    val uiState: StateFlow<CoinUiState> = _uiState.asStateFlow()

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
                delay(60_000)
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

        getCoinsUseCase().fold(
            onSuccess = { coins ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    coins = coins,
                    lastUpdated = getCurrentTime()
                )
            },
            onFailure = { error ->
                val hasCachedData = _uiState.value.coins.isNotEmpty()
                val message = when {
                    hasCachedData -> null
                    error.message?.contains("429") == true -> "Rate limit exceeded. Please wait a moment."
                    error.message?.contains("Rate Limit") == true -> "Rate limit exceeded. Please wait a moment."
                    else -> error.message ?: "Unknown error"
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = message
                )
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

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onSearchActiveChange(isActive: Boolean) {
        _uiState.value = _uiState.value.copy(
            isSearchActive = isActive,
            searchQuery = if (!isActive) "" else _uiState.value.searchQuery
        )
    }

    override fun onCleared() {
        super.onCleared()
        pollingJob?.cancel()
        cooldownJob?.cancel()
    }
}