package com.revive.coinpulse.domain.usecase

import com.revive.coinpulse.domain.repository.CoinRepository

class ToggleFavoriteUseCase(private val repository: CoinRepository) {
    operator fun invoke(coinId: String) = repository.toggleFavorite(coinId)
}