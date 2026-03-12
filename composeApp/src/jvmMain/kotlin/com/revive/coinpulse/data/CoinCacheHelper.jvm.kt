package com.revive.coinpulse.data

import com.russhwolf.settings.Settings
import java.io.File

private val cacheFile = File(System.getProperty("user.home"), ".coinpulse/coin_cache.json")
private val timeFile = File(System.getProperty("user.home"), ".coinpulse/cache_time.txt")

actual fun saveCoinCache(data: String, time: Long) {
    cacheFile.parentFile?.mkdirs()
    cacheFile.writeText(data)
    timeFile.writeText(time.toString())
}
actual fun loadCoinCache(): String = if (cacheFile.exists()) cacheFile.readText() else ""
actual fun loadCoinCacheTime(): Long = if (timeFile.exists()) timeFile.readText().toLongOrNull() ?: 0L else 0L
actual fun clearCoinCache() {
    cacheFile.delete()
    timeFile.delete()
}

actual fun initCoinCacheSettings(settings: Settings) {
}