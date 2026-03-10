package com.revive.coinpulse

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CoinPulse",
    ) {
        App()
    }
}