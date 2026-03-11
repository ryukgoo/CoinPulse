package com.revive.coinpulse.domain.repository

import com.revive.coinpulse.data.model.Coin
import kotlinx.coroutines.flow.StateFlow

interface CoinRepository {
    suspend fun getCoins(
        currency: String = "usd",
        page: Int = 1,
        perPage: Int = 100
    ): Result<List<Coin>>

    val favoritesFlow: StateFlow<Set<String>>
    fun isFavorite(coinId: String): Boolean
    fun toggleFavorite(coinId: String)
    fun loadCachedCoins(): List<Coin>
    fun hasCachedData(): Boolean
    fun getCacheTime(): String
    fun clearCache()
}