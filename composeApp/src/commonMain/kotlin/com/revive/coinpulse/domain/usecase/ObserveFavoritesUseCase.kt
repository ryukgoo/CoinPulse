package com.revive.coinpulse.domain.usecase

import com.revive.coinpulse.domain.repository.CoinRepository
import kotlinx.coroutines.flow.StateFlow

class ObserveFavoritesUseCase(private val repository: CoinRepository) {
    operator fun invoke(): StateFlow<Set<String>> = repository.favoritesFlow
}