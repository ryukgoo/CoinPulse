package com.revive.coinpulse.presentation.navigation

import androidx.compose.runtime.Composable
import com.revive.coinpulse.isMobile
import com.revive.coinpulse.presentation.ui.WindowSizeClass
import com.revive.coinpulse.presentation.ui.getWindowWidth
import com.revive.coinpulse.presentation.ui.toWindowSizeClass
import com.revive.coinpulse.presentation.viewmodel.CoinViewModel

@Composable
fun AppNavigation(viewModel: CoinViewModel) {
    val windowWidth = getWindowWidth()
    val windowSizeClass = windowWidth.toWindowSizeClass()

    if (isMobile || windowSizeClass == WindowSizeClass.Compact) {
        MobileNavigation(viewModel = viewModel)
    } else {
        AdaptiveNavigation(
            viewModel = viewModel,
            windowSizeClass = windowSizeClass
        )
    }
}