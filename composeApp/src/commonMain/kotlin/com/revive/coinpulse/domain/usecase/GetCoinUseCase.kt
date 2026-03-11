package com.revive.coinpulse.domain.usecase

import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.domain.repository.CoinRepository

class GetCoinsUseCase(private val repository: CoinRepository) {
    suspend operator fun invoke(
        currency: String = "usd",
        page: Int = 1,
        perPage: Int = 100
    ): Result<List<Coin>> = repository.getCoins(currency, page, perPage)
}