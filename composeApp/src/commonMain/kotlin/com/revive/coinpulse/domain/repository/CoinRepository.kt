package com.revive.coinpulse.domain.repository

import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.data.model.PricePoint
import kotlinx.coroutines.flow.StateFlow

interface CoinRepository {
    suspend fun getCoins(currency: String, page: Int, perPage: Int): Result<List<Coin>>
    suspend fun getMarketChart(coinId: String, currency: String): Result<List<PricePoint>>
    val favoritesFlow: StateFlow<Set<String>>
    fun isFavorite(coinId: String): Boolean
    fun toggleFavorite(coinId: String)
    fun loadCachedCoins(): List<Coin>
    fun hasCachedData(): Boolean
    fun getCacheTime(): String
    fun clearCache()
}