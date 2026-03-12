package com.revive.coinpulse.data

import com.russhwolf.settings.Settings

lateinit var cacheSettings: Settings

actual fun initCoinCacheSettings(settings: Settings) {
    cacheSettings = settings
}
actual fun saveCoinCache(data: String, time: Long) {
    cacheSettings.putString("coin_cache", data)
    cacheSettings.putLong("cache_time", time)
}
actual fun loadCoinCache(): String = cacheSettings.getString("coin_cache", "")
actual fun loadCoinCacheTime(): Long = cacheSettings.getLong("cache_time", 0L)
actual fun clearCoinCache() {
    cacheSettings.remove("coin_cache")
    cacheSettings.remove("cache_time")
}