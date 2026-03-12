package com.revive.coinpulse.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.revive.coinpulse.data.AppTheme

@Immutable
data class CoinPulseColorScheme(
    val Background: Color,
    val Surface: Color,
    val Primary: Color,
    val TextPrimary: Color,
    val TextSecondary: Color,
    val PriceUp: Color,
    val PriceDown: Color
)

val DarkColorScheme = CoinPulseColorScheme(
    Background = Color(0xFF0D1117),
    Surface = Color(0xFF161B22),
    Primary = Color(0xFF58A6FF),
    TextPrimary = Color(0xFFE6EDF3),
    TextSecondary = Color(0xFF8B949E),
    PriceUp = Color(0xFF00C853),
    PriceDown = Color(0xFFFF1744)
)

val LightColorScheme = CoinPulseColorScheme(
    Background = Color(0xFFF6F8FA),
    Surface = Color(0xFFFFFFFF),
    Primary = Color(0xFF0969DA),
    TextPrimary = Color(0xFF1F2328),
    TextSecondary = Color(0xFF656D76),
    PriceUp = Color(0xFF1A7F37),
    PriceDown = Color(0xFFCF222E)
)

val LocalCoinPulseColors = staticCompositionLocalOf { DarkColorScheme }

object CoinPulseColors {
    val current: CoinPulseColorScheme
        @Composable
        get() = LocalCoinPulseColors.current

    val Background: Color
        @Composable get() = LocalCoinPulseColors.current.Background
    val Surface: Color
        @Composable get() = LocalCoinPulseColors.current.Surface
    val Primary: Color
        @Composable get() = LocalCoinPulseColors.current.Primary
    val TextPrimary: Color
        @Composable get() = LocalCoinPulseColors.current.TextPrimary
    val TextSecondary: Color
        @Composable get() = LocalCoinPulseColors.current.TextSecondary
    val PriceUp: Color
        @Composable get() = LocalCoinPulseColors.current.PriceUp
    val PriceDown: Color
        @Composable get() = LocalCoinPulseColors.current.PriceDown
}

@Composable
fun CoinPulseTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    content: @Composable () -> Unit
) {
    val isDark = when (appTheme) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalCoinPulseColors provides colorScheme) {
        MaterialTheme(content = content)
    }
}