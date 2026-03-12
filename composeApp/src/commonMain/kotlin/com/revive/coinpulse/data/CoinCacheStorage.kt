package com.revive.coinpulse.data

import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.formatEpochMs
import com.revive.coinpulse.getCurrentEpochMs
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json

class CoinCacheStorage(settings: Settings) {
    init {
        initCoinCacheSettings(settings)
    }

    private val json = Json { ignoreUnknownKeys = true }

    fun saveCoins(coins: List<Coin>) {
        saveCoinCache(json.encodeToString(coins), getCurrentEpochMs())
    }

    fun loadCoins(): List<Coin> {
        val raw = loadCoinCache()
        return if (raw.isEmpty()) {
            emptyList()
        } else {
            json.decodeFromString(raw)
        }
    }

    fun hasCachedData(): Boolean = loadCoinCache().isNotEmpty()

    fun getCacheTime(): String {
        val epochMs = loadCoinCacheTime()
        return if (epochMs == 0L) "" else formatEpochMs(epochMs)
    }

    fun clearCache() = clearCoinCache()
}
