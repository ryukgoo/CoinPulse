package com.revive.coinpulse.domain.usecase

import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.domain.repository.CoinRepository

class GetCachedCoinsUseCase(private val repository: CoinRepository) {
    operator fun invoke(): List<Coin> = repository.loadCachedCoins()

    fun hasCachedData(): Boolean = repository.hasCachedData()

    fun getCacheTime(): String = repository.getCacheTime()

    fun clearCache() = repository.clearCache()
}
