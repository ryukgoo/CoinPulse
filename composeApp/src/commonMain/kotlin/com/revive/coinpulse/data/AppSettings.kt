package com.revive.coinpulse.data

import com.russhwolf.settings.Settings

class AppSettings(private val settings: Settings) {

    companion object {
        private const val KEY_CURRENCY = "currency"
        private const val KEY_REFRESH_INTERVAL = "refresh_interval"
        private const val KEY_COIN_COUNT = "coin_count"

        val CURRENCIES = listOf("usd", "krw", "eur", "jpy", "btc")
        val REFRESH_INTERVALS = listOf(30L, 60L, 180L, 300L)
        val COIN_COUNTS = listOf(50, 100, 250)

        fun refreshIntervalLabel(seconds: Long): String = when (seconds) {
            30L -> "30초"
            60L -> "1분"
            180L -> "3분"
            300L -> "5분"
            else -> "1분"
        }

        fun coinCountLabel(count: Int): String = "${count}개"
    }

    var currency: String
        get() = settings.getString(KEY_CURRENCY, "usd")
        set(value) = settings.putString(KEY_CURRENCY, value)

    var refreshInterval: Long
        get() = settings.getLong(KEY_REFRESH_INTERVAL, 60L)
        set(value) = settings.putLong(KEY_REFRESH_INTERVAL, value)

    var coinCount: Int
        get() = settings.getInt(KEY_COIN_COUNT, 100)
        set(value) = settings.putInt(KEY_COIN_COUNT, value)
}