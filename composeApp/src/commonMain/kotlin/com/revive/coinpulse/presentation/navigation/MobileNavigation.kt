package com.revive.coinpulse.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.revive.coinpulse.presentation.ui.screen.CoinDetailScreen
import com.revive.coinpulse.presentation.ui.screen.CoinFavoriteScreen
import com.revive.coinpulse.presentation.ui.screen.CoinListScreen
import com.revive.coinpulse.presentation.ui.screen.SettingsScreen
import com.revive.coinpulse.presentation.ui.theme.CoinPulseColors
import com.revive.coinpulse.presentation.viewmodel.CoinViewModel
import kotlinx.serialization.Serializable

@Serializable
data class CoinDetailRoute(val coinId: String)

@Composable
fun MobileNavigation(viewModel: CoinViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute?.contains("CoinDetailRoute") == false

    Scaffold(
        containerColor = CoinPulseColors.Background,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = CoinPulseColors.Surface) {
                    BottomNavItem.entries.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute?.contains(item.route) == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                )
                            },
                            label = { Text(text = item.label) },
                            colors =
                                NavigationBarItemDefaults.colors(
                                    selectedIconColor = CoinPulseColors.Primary,
                                    selectedTextColor = CoinPulseColors.Primary,
                                    unselectedIconColor = CoinPulseColors.TextSecondary,
                                    unselectedTextColor = CoinPulseColors.TextSecondary,
                                    indicatorColor = CoinPulseColors.Background,
                                ),
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(BottomNavItem.Home.route) {
                CoinListScreen(
                    viewModel = viewModel,
                    onCoinClick = { coinId ->
                        navController.navigate(CoinDetailRoute(coinId))
                    },
                )
            }
            composable(BottomNavItem.Favorites.route) {
                CoinFavoriteScreen(
                    viewModel = viewModel,
                    onCoinClick = { coinId ->
                        navController.navigate(CoinDetailRoute(coinId))
                    },
                )
            }
            composable(BottomNavItem.Settings.route) {
                SettingsScreen(viewModel = viewModel)
            }
            composable<CoinDetailRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<CoinDetailRoute>()
                val coin = viewModel.uiState.value.coins.find { it.id == route.coinId } ?: return@composable
                CoinDetailScreen(
                    coin = coin,
                    currency = viewModel.uiState.value.currency,
                    chartData = viewModel.uiState.value.chartData,
                    isChartLoading = viewModel.uiState.value.isChartLoading,
                    onLoadChart = { viewModel.loadChart(it) },
                    onBackClick = { navController.popBackStack() },
                )
            }
        }
    }
}
