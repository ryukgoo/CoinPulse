package com.revive.coinpulse.data

import com.russhwolf.settings.Settings

actual fun saveCoinCache(
    data: String,
    time: Long,
) {}

actual fun loadCoinCache(): String = ""

actual fun loadCoinCacheTime(): Long = 0L

actual fun clearCoinCache() {}

actual fun initCoinCacheSettings(settings: Settings) {
}
