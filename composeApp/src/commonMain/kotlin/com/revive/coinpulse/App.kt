package com.revive.coinpulse

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.revive.coinpulse.di.appModule
import com.revive.coinpulse.presentation.navigation.AppNavigation
import com.revive.coinpulse.presentation.ui.theme.CoinPulseTheme
import com.revive.coinpulse.presentation.viewmodel.CoinViewModel
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        val viewModel = koinViewModel<CoinViewModel>()
        val settingsUiState by viewModel.settingsUiState.collectAsState()

        CoinPulseTheme(appTheme = settingsUiState.theme) {
            AppNavigation(viewModel = viewModel)
        }
    }
}