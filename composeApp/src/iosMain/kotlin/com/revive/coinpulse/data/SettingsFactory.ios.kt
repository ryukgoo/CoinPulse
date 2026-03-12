package com.revive.coinpulse.data

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings

actual fun createSettings(): Settings {
    return NSUserDefaultsSettings.Factory().create("coinpulse_prefs")
}
