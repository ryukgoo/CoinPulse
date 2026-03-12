package com.revive.coinpulse.data.remote

object CoinGeckoApi {
    const val BASE_URL = "https://api.coingecko.com/api/v3"

    object Endpoints {
        const val COINS_MARKETS = "/coins/markets"
        const val MARKET_CHART = "/coins/{id}/market_chart"
    }

    object Params {
        const val VS_CURRENCY = "vs_currency"
        const val ORDER = "order"
        const val PER_PAGE = "per_page"
        const val PAGE = "page"
        const val SPARKLINE = "sparkline"
        const val DAYS = "days"
    }

    object Defaults {
        const val CURRENCY = "usd"
        const val ORDER = "market_cap_desc"
        const val PER_PAGE = 100
        const val PAGE = 1
        const val SPARKLINE = false
        const val CHART_DAYS = 7
    }
}
