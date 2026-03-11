package com.revive.coinpulse

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.revive.coinpulse.di.appModule
import com.revive.coinpulse.presentation.navigation.AppNavigation
import com.revive.coinpulse.presentation.ui.theme.CoinPulseTheme
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        CoinPulseTheme {
            AppNavigation()
        }
    }
}