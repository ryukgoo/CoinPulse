package com.revive.coinpulse.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.revive.coinpulse.data.CoinRepositoryImpl
import com.revive.coinpulse.data.remote.CoinRemoteDataSource
import com.revive.coinpulse.data.remote.createHttpClient
import com.revive.coinpulse.presentation.ui.screen.CoinDetailScreen
import com.revive.coinpulse.presentation.ui.screen.CoinListScreen
import com.revive.coinpulse.presentation.viewmodel.CoinViewModel
import kotlinx.serialization.Serializable

@Serializable
object CoinListRoute

@Serializable
data class CoinDetailRoute(val coinId: String)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val httpClient = createHttpClient()
    val remoteDataSource = CoinRemoteDataSource(httpClient)
    val repository = CoinRepositoryImpl(remoteDataSource)
    val viewModel = CoinViewModel(repository)

    NavHost(
        navController = navController,
        startDestination = CoinListRoute
    ) {
        composable<CoinListRoute> {
            CoinListScreen(
                viewModel = viewModel,
                onCoinClick = { coinId ->
                    navController.navigate(CoinDetailRoute(coinId))
                }
            )
        }
        composable<CoinDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CoinDetailRoute>()
            val coin = viewModel.uiState.value.coins.find { it.id == route.coinId } ?: return@composable
            CoinDetailScreen(
                coin = coin,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}