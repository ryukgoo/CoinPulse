package com.revive.coinpulse.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.revive.coinpulse.presentation.ui.WindowSizeClass
import com.revive.coinpulse.presentation.ui.component.SideNavBar
import com.revive.coinpulse.presentation.ui.screen.CoinDetailContent
import com.revive.coinpulse.presentation.ui.screen.CoinFavoriteContent
import com.revive.coinpulse.presentation.ui.screen.CoinListContent
import com.revive.coinpulse.presentation.ui.screen.SettingsContent
import com.revive.coinpulse.presentation.ui.theme.CoinPulseColors
import com.revive.coinpulse.presentation.viewmodel.CoinViewModel

@Composable
fun AdaptiveNavigation(
    viewModel: CoinViewModel,
    windowSizeClass: WindowSizeClass
) {
    val uiState by viewModel.uiState.collectAsState()
    val settingsUiState by viewModel.settingsUiState.collectAsState()

    var selectedRoute by remember { mutableStateOf(BottomNavItem.Home.route) }
    var selectedCoinId by remember { mutableStateOf<String?>(null) }
    var isSideNavExpanded by remember { mutableStateOf(true) }

    val isExpanded = windowSizeClass == WindowSizeClass.Expanded

    Scaffold(containerColor = CoinPulseColors.Background) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // SideNavBar
            SideNavBar(
                currentRoute = selectedRoute,
                onItemClick = { item ->
                    selectedRoute = item.route
                    selectedCoinId = null
                },
                isExpanded = isSideNavExpanded,
                onToggle = { isSideNavExpanded = !isSideNavExpanded }
            )

            // 마스터 영역
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                // Medium일 때 디테일이 마스터를 덮음
                if (!isExpanded && selectedCoinId != null) {
                    val coin = uiState.coins.find { it.id == selectedCoinId }
                    if (coin != null) {
                        CoinDetailContent(
                            coin = coin,
                            currency = uiState.currency,
                            onBackClick = { selectedCoinId = null }
                        )
                        return@Box
                    }
                }

                when (selectedRoute) {
                    BottomNavItem.Home.route -> CoinListContent(
                        uiState = uiState,
                        onCoinClick = { selectedCoinId = it },
                        onFavoriteClick = { viewModel.toggleFavorite(it) },
                        onRefresh = { viewModel.refresh() },
                        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                        onSearchActiveChange = { viewModel.onSearchActiveChange(it) }
                    )
                    BottomNavItem.Favorites.route -> CoinFavoriteContent(
                        favoriteCoins = uiState.coins.filter { uiState.favorites.contains(it.id) },
                        favorites = uiState.favorites,
                        onCoinClick = { selectedCoinId = it },
                        onFavoriteClick = { viewModel.toggleFavorite(it) }
                    )
                    BottomNavItem.Settings.route -> SettingsContent(
                        settingsUiState = settingsUiState,
                        onCurrencyChange = { viewModel.onCurrencyChange(it) },
                        onRefreshIntervalChange = { viewModel.onRefreshIntervalChange(it) },
                        onCoinCountChange = { viewModel.onCoinCountChange(it) },
                        onThemeChange = { viewModel.onThemeChange(it) }
                    )
                }
            }

            // 디테일 영역 - Expanded에서만 표시
            if (isExpanded) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(CoinPulseColors.Surface)
                ) {
                    val coin = selectedCoinId?.let { id ->
                        uiState.coins.find { it.id == id }
                    }
                    if (coin != null) {
                        CoinDetailContent(
                            coin = coin,
                            currency = uiState.currency,
                            onBackClick = { selectedCoinId = null }
                        )
                    } else {
                        EmptyDetailPlaceholder()
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyDetailPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = "Select a coin to view details",
            color = CoinPulseColors.TextSecondary
        )
    }
}