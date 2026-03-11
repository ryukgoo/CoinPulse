package com.revive.coinpulse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.domain.repository.CoinRepository
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
    private val repository: CoinRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoinUiState())
    val uiState: StateFlow<CoinUiState> = _uiState.asStateFlow()

    private var pollingJob: Job? = null
    private var cooldownJob: Job? = null

    companion object {
        const val POLLING_INTERVAL_MS = 60_000L
        const val COOLDOWN_MS = 60_000L
    }

    init {
        loadCache()
        startPolling()
        observeFavorites()
    }

    private fun loadCache() {
        if (repository.hasCachedData()) {
            _uiState.value = _uiState.value.copy(
                coins = repository.loadCachedCoins(),
                lastUpdated = repository.getCacheTime()
            )
        }
    }

    private suspend fun getCoins() {
        _uiState.value = _uiState.value.copy(
            isLoading = _uiState.value.coins.isEmpty(),
            errorMessage = null
        )

        repository.getCoins().fold(
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
                    hasCachedData -> null // 캐시 데이터가 있으면 에러 표시 안함
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

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onSearchActiveChange(isActive: Boolean) {
        _uiState.value = _uiState.value.copy(
            isSearchActive = isActive,
            searchQuery = if (!isActive) "" else _uiState.value.searchQuery
        )
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.favoritesFlow.collect { favorites ->
                _uiState.value = _uiState.value.copy(favorites = favorites)
            }
        }
    }

    fun toggleFavorite(coinId: String) {
        repository.toggleFavorite(coinId)
    }

    fun startPolling() {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (true) {
                getCoins()
                delay(POLLING_INTERVAL_MS)
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
    }

    fun refresh() {
        if (!_uiState.value.isRefreshEnabled) return
        pollingJob?.cancel()
        startCooldown()
        startPolling()
    }

    private fun startCooldown() {
        cooldownJob?.cancel()
        _uiState.value = _uiState.value.copy(isRefreshEnabled = false)
        cooldownJob = viewModelScope.launch {
            delay(COOLDOWN_MS)
            _uiState.value = _uiState.value.copy(isRefreshEnabled = true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
        cooldownJob?.cancel()
    }
}