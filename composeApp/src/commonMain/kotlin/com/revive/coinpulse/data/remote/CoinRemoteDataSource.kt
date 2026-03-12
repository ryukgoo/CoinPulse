package com.revive.coinpulse.data.remote

import com.revive.coinpulse.data.model.ChartData
import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.data.model.PricePoint
import com.revive.coinpulse.data.model.toPricePoints
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class CoinRemoteDataSource(private val httpClient: HttpClient) {
    suspend fun getCoins(
        currency: String = CoinGeckoApi.Defaults.CURRENCY,
        page: Int = CoinGeckoApi.Defaults.PAGE,
        perPage: Int = CoinGeckoApi.Defaults.PER_PAGE,
    ): List<Coin> {
        return httpClient.get(
            CoinGeckoApi.BASE_URL + CoinGeckoApi.Endpoints.COINS_MARKETS,
        ) {
            parameter(CoinGeckoApi.Params.VS_CURRENCY, currency)
            parameter(CoinGeckoApi.Params.ORDER, CoinGeckoApi.Defaults.ORDER)
            parameter(CoinGeckoApi.Params.PER_PAGE, perPage)
            parameter(CoinGeckoApi.Params.PAGE, page)
            parameter(CoinGeckoApi.Params.SPARKLINE, CoinGeckoApi.Defaults.SPARKLINE)
        }.body()
    }

    suspend fun getMarketChart(
        coinId: String,
        currency: String = CoinGeckoApi.Defaults.CURRENCY,
    ): List<PricePoint> {
        return httpClient.get(
            CoinGeckoApi.BASE_URL +
                CoinGeckoApi.Endpoints.MARKET_CHART.replace("{id}", coinId),
        ) {
            parameter(CoinGeckoApi.Params.VS_CURRENCY, currency)
            parameter(CoinGeckoApi.Params.DAYS, CoinGeckoApi.Defaults.CHART_DAYS)
        }.body<ChartData>().toPricePoints()
    }
}
