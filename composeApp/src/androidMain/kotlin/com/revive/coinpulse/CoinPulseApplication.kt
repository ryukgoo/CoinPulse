package com.revive.coinpulse

import android.app.Application
import com.revive.coinpulse.data.appContext

class CoinPulseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}
