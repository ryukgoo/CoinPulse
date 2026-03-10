package com.revive.coinpulse.data

import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.data.remote.CoinRemoteDataSource
import com.revive.coinpulse.domain.repository.CoinRepository

class CoinRepositoryImpl(
    private val remoteDataSource: CoinRemoteDataSource
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
            Result.success(coins)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}