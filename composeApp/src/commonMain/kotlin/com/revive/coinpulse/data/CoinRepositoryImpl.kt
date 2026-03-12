package com.revive.coinpulse.data

import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.data.model.PricePoint
import com.revive.coinpulse.data.remote.CoinRemoteDataSource
import com.revive.coinpulse.domain.repository.CoinRepository
import kotlinx.coroutines.flow.StateFlow

class CoinRepositoryImpl(
    private val remoteDataSource: CoinRemoteDataSource,
    private val favoriteStorage: FavoriteStorage,
    private val cacheStorage: CoinCacheStorage
) : CoinRepository {

    override suspend fun getCoins(
        currency: String,
        page: Int,
        perPage: Int
    ): Result<List<Coin>> {
        return try {
            val coins = remoteDataSource.getCoins(
                currency = currency,
                page = page,
                perPage = perPage
            )
            cacheStorage.saveCoins(coins)
            Result.success(coins)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMarketChart(
        coinId: String,
        currency: String
    ): Result<List<PricePoint>> {
        return try {
            val points = remoteDataSource.getMarketChart(coinId, currency)
            Result.success(points)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override val favoritesFlow: StateFlow<Set<String>> = favoriteStorage.favoritesFlow
    override fun isFavorite(coinId: String): Boolean = favoriteStorage.isFavorite(coinId)
    override fun toggleFavorite(coinId: String) = favoriteStorage.toggleFavorite(coinId)
    override fun loadCachedCoins(): List<Coin> = cacheStorage.loadCoins()
    override fun hasCachedData(): Boolean = cacheStorage.hasCachedData()
    override fun getCacheTime(): String = cacheStorage.getCacheTime()
    override fun clearCache() = cacheStorage.clearCache()
}