package com.revive.coinpulse.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    @SerialName("current_price") val currentPrice: Double = 0.0,
    @SerialName("price_change_percentage_24h") val priceChangePercentage24h: Double? = null,
    @SerialName("market_cap") val marketCap: Double = 0.0,
    @SerialName("total_volume") val totalVolume: Double? = null,
    @SerialName("image") val imageUrl: String = "",
    @SerialName("market_cap_rank") val marketCapRank: Int? = null,
    @SerialName("high_24h") val high24h: Double? = null,
    @SerialName("low_24h") val low24h: Double? = null,
)
