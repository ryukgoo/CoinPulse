package com.revive.coinpulse

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.revive.coinpulse.presentation.navigation.AppNavigation
import com.revive.coinpulse.presentation.ui.theme.CoinPulseTheme

@Composable
@Preview
fun App() {
    CoinPulseTheme {
        AppNavigation()
    }
}