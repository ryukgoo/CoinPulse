package com.revive.coinpulse.domain.usecase

import com.revive.coinpulse.data.model.PricePoint
import com.revive.coinpulse.domain.repository.CoinRepository

class GetMarketChartUseCase(private val repository: CoinRepository) {
    suspend operator fun invoke(
        coinId: String,
        currency: String
    ): Result<List<PricePoint>> = repository.getMarketChart(coinId, currency)
}