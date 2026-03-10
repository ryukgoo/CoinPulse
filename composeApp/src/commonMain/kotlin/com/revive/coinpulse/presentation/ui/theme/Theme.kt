package com.revive.coinpulse.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object CoinPulseColors {
    val Background = Color(0xFF0D1117)
    val Surface = Color(0xFF161B22)
    val Primary = Color(0xFF58A6FF)
    val PriceUp = Color(0xFF00C853)
    val PriceDown = Color(0xFFFF1744)
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFF8B949E)
}

private val DarkColorScheme = darkColorScheme(
    background = CoinPulseColors.Background,
    surface = CoinPulseColors.Surface,
    primary = CoinPulseColors.Primary,
    onBackground = CoinPulseColors.TextPrimary,
    onSurface = CoinPulseColors.TextPrimary,
)

@Composable
fun CoinPulseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}