package com.revive.coinpulse.data

import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.formatEpochMs
import com.revive.coinpulse.getCurrentEpochMs
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json

class CoinCacheStorage(private val settings: Settings) {

    companion object {
        private const val KEY_COIN_CACHE = "coin_cache"
        private const val KEY_CACHE_TIME = "cache_time"
    }

    private val json = Json { ignoreUnknownKeys = true }

    fun saveCoins(coins: List<Coin>) {
        settings.putString(KEY_COIN_CACHE, json.encodeToString(coins))
        settings.putLong(KEY_CACHE_TIME, getCurrentEpochMs())
    }

    fun loadCoins(): List<Coin> {
        val raw = settings.getString(KEY_COIN_CACHE, "")
        return if (raw.isEmpty()) emptyList()
        else json.decodeFromString(raw)
    }

    fun hasCachedData(): Boolean {
        return settings.getString(KEY_COIN_CACHE, "").isNotEmpty()
    }

    fun getCacheTime(): String {
        val epochMs = settings.getLong(KEY_CACHE_TIME, 0L)
        return if (epochMs == 0L) "" else formatEpochMs(epochMs)
    }
}