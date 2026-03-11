package com.revive.coinpulse

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.Instant

fun getCurrentTime(): String {
    return formatEpochMs(Clock.System.now().toEpochMilliseconds())
}

fun getCurrentEpochMs(): Long = Clock.System.now().toEpochMilliseconds()

fun formatEpochMs(epochMs: Long): String {
    val instant = Instant.fromEpochMilliseconds(epochMs)
    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}:${local.second.toString().padStart(2, '0')}"
}

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