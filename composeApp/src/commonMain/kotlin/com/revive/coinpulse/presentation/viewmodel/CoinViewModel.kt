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
    val isRefreshEnabled: Boolean = true
)

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
        startPolling()
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
                val message = when {
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

    override fun onCleared() {
        super.onCleared()
        stopPolling()
        cooldownJob?.cancel()
    }
}