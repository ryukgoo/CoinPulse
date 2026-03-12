package com.revive.coinpulse.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChartData(
    @SerialName("prices") val prices: List<List<Double>>
)

data class PricePoint(
    val timestamp: Long,
    val price: Double
)

fun ChartData.toPricePoints(): List<PricePoint> =
    prices.map { PricePoint(it[0].toLong(), it[1]) }