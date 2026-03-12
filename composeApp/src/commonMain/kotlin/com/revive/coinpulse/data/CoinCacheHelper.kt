package com.revive.coinpulse.data

import com.russhwolf.settings.Settings

expect fun initCoinCacheSettings(settings: Settings)

expect fun saveCoinCache(
    data: String,
    time: Long,
)

expect fun loadCoinCache(): String

expect fun loadCoinCacheTime(): Long

expect fun clearCoinCache()
