package com.revive.coinpulse.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.presentation.ui.theme.CoinPulseColors
import com.revive.coinpulse.presentation.ui.theme.CoinPulseTheme
import com.revive.coinpulse.presentation.viewmodel.CoinViewModel

@Composable
fun CoinFavoriteScreen(
    viewModel: CoinViewModel,
    onCoinClick: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val favoriteCoins = uiState.coins.filter { uiState.favorites.contains(it.id) }

    CoinFavoriteContent(
        favoriteCoins = favoriteCoins,
        favorites = uiState.favorites,
        onCoinClick = onCoinClick,
        onFavoriteClick = { viewModel.toggleFavorite(it) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinFavoriteContent(
    favoriteCoins: List<Coin>,
    favorites: Set<String>,
    onCoinClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(CoinPulseColors.Background),
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Favorites",
                    color = CoinPulseColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                )
            },
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = CoinPulseColors.Background,
                ),
        )

        if (favoriteCoins.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "No favorites yet.\nTap ⭐ to add coins!",
                    color = CoinPulseColors.TextSecondary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            val listState = rememberLazyListState()
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(favoriteCoins) { coin ->
                        CoinItem(
                            coin = coin,
                            isFavorite = favorites.contains(coin.id),
                            onCoinClick = { onCoinClick(coin.id) },
                            onFavoriteClick = { onFavoriteClick(coin.id) },
                        )
                    }
                }
                CoinScrollbar(
                    listState = listState,
                    modifier =
                        Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight(),
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "CoinFavorite - Data")
@Composable
fun CoinFavoriteContentDataPreview() {
    val dummyCoins =
        listOf(
            Coin(
                id = "bitcoin",
                symbol = "btc",
                name = "Bitcoin",
                currentPrice = 70000.0,
                priceChangePercentage24h = 4.61,
                marketCap = 1380000000000.0,
                totalVolume = 28000000000.0,
                imageUrl = "",
                marketCapRank = 1,
                high24h = 71000.0,
                low24h = 69000.0,
            ),
            Coin(
                id = "ethereum",
                symbol = "eth",
                name = "Ethereum",
                currentPrice = 2066.49,
                priceChangePercentage24h = -3.40,
                marketCap = 248000000000.0,
                totalVolume = 12000000000.0,
                imageUrl = "",
                marketCapRank = 2,
                high24h = 2100.0,
                low24h = 2000.0,
            ),
        )

    CoinPulseTheme {
        CoinFavoriteContent(
            favoriteCoins = dummyCoins,
            favorites = setOf("bitcoin", "ethereum"),
            onCoinClick = {},
            onFavoriteClick = {},
        )
    }
}

@Preview(showBackground = true, name = "CoinFavorite - Empty")
@Composable
fun CoinFavoriteContentEmptyPreview() {
    CoinPulseTheme {
        CoinFavoriteContent(
            favoriteCoins = emptyList(),
            favorites = emptySet(),
            onCoinClick = {},
            onFavoriteClick = {},
        )
    }
}
