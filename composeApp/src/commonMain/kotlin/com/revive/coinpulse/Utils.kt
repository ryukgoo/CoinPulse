package com.revive.coinpulse

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.time.Clock

fun Double.toFormattedPrice(): String {
    val long = this.toLong()
    val decimal = ((this - long) * 100).roundToInt()
    val decimalStr = decimal.toString().padStart(2, '0')

    val intStr = long.toString()
    val withComma = buildString {
        intStr.reversed().forEachIndexed { index, c ->
            if (index > 0 && index % 3 == 0) append(',')
            append(c)
        }
    }.reversed()

    return "$$withComma.$decimalStr"
}

fun Double.toFormattedPercent(): String {
    val absVal = abs(this)
    val long = absVal.toLong()
    val decimal = ((absVal - long) * 100).roundToInt()
    val decimalStr = decimal.toString().padStart(2, '0')
    val sign = if (this >= 0) "+" else "-"
    return "$sign$long.$decimalStr%"
}

fun getCurrentTime(): String {
    val now = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
    return "${now.hour.toString().padStart(2, '0')}:${now.minute.toString().padStart(2, '0')}:${now.second.toString().padStart(2, '0')}"
}