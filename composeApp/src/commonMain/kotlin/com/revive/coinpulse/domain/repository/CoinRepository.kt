package com.revive.coinpulse.domain.repository

import com.revive.coinpulse.data.model.Coin

interface CoinRepository {
    suspend fun getCoins(
        currency: String = "usd",
        page: Int = 1,
        perPage: Int = 100
    ): Result<List<Coin>>
}