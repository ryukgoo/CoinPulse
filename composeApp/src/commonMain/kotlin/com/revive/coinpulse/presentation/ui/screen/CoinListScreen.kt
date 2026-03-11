package com.revive.coinpulse.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.isMobile
import com.revive.coinpulse.presentation.ui.theme.CoinPulseColors
import com.revive.coinpulse.presentation.viewmodel.CoinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinListScreen(
    viewModel: CoinViewModel,
    onCoinClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CoinPulseColors.Background)
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "CoinPulse",
                        color = CoinPulseColors.TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    if (uiState.lastUpdated.isNotEmpty()) {
                        Text(
                            text = "Updated: ${uiState.lastUpdated}",
                            color = CoinPulseColors.TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            },
            actions = {
                IconButton(
                    onClick = { viewModel.refresh() },
                    enabled = uiState.isRefreshEnabled
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = if (uiState.isRefreshEnabled)
                            CoinPulseColors.TextPrimary
                        else
                            CoinPulseColors.TextSecondary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = CoinPulseColors.Background
            )
        )

        when {
            uiState.isLoading && uiState.coins.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CoinPulseColors.Primary)
                }
            }
            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.errorMessage!!,
                        color = CoinPulseColors.PriceDown
                    )
                }
            }
            else -> {
                if (isMobile) {
                    PullToRefreshBox(
                        isRefreshing = uiState.isLoading,
                        onRefresh = { viewModel.refresh() },
                        state = pullToRefreshState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CoinLazyList(
                            coins = uiState.coins,
                            onCoinClick = onCoinClick
                        )
                    }
                } else {
                    CoinLazyList(
                        coins = uiState.coins,
                        onCoinClick = onCoinClick
                    )
                }
            }
        }
    }
}

@Composable
private fun CoinLazyList(
    coins: List<Coin>,
    onCoinClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { SummaryCard(coins = coins) }
        items(coins) { coin ->
            CoinItem(
                coin = coin,
                onCoinClick = { onCoinClick(coin.id) }
            )
        }
    }
}