package com.revive.coinpulse.presentation.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class WindowSizeClass {
    Compact, // < 600dp  (Mobile)
    Medium, // 600~960dp (Tablet)
    Expanded, // > 960dp (Desktop/Web)
}

fun Dp.toWindowSizeClass(): WindowSizeClass =
    when {
        this < 600.dp -> WindowSizeClass.Compact
        this < 960.dp -> WindowSizeClass.Medium
        else -> WindowSizeClass.Expanded
    }
