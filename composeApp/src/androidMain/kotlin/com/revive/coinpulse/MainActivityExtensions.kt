package com.revive.coinpulse

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.revive.coinpulse.data.AppTheme
import com.revive.coinpulse.di.appModule
import com.revive.coinpulse.presentation.navigation.AppNavigation
import com.revive.coinpulse.presentation.ui.theme.CoinPulseTheme
import com.revive.coinpulse.presentation.viewmodel.CoinViewModel
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppWithSystemUI() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        val viewModel = koinViewModel<CoinViewModel>()
        val settingsUiState by viewModel.settingsUiState.collectAsState()

        CoinPulseTheme(appTheme = settingsUiState.theme) {
            val isDark =
                when (settingsUiState.theme) {
                    AppTheme.DARK -> true
                    AppTheme.LIGHT -> false
                    AppTheme.SYSTEM -> isSystemInDarkTheme()
                }
            val context = LocalContext.current
            val activity = context as? Activity

            SideEffect {
                activity?.window?.let { window ->
                    WindowCompat.setDecorFitsSystemWindows(window, false)
                    WindowCompat.getInsetsController(window, window.decorView)
                        .isAppearanceLightStatusBars = !isDark
                }
            }
            AppNavigation(viewModel = viewModel)
        }
    }
}
