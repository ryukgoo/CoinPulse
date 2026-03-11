package com.revive.coinpulse.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.isMobile
import com.revive.coinpulse.presentation.ui.theme.CoinPulseColors
import com.revive.coinpulse.presentation.ui.theme.CoinPulseTheme
import com.revive.coinpulse.presentation.viewmodel.CoinUiState
import com.revive.coinpulse.presentation.viewmodel.CoinViewModel

@Composable
fun CoinListScreen(
    viewModel: CoinViewModel,
    onCoinClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.onSnackbarDismissed()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = CoinPulseColors.Surface,
                    contentColor = CoinPulseColors.TextPrimary
                )
            }
        },
        containerColor = CoinPulseColors.Background
    ) { innerPadding ->
        CoinListContent(
            uiState = uiState,
            onCoinClick = onCoinClick,
            onFavoriteClick = { viewModel.toggleFavorite(it) },
            onRefresh = { viewModel.refresh() },
            onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
            onSearchActiveChange = { viewModel.onSearchActiveChange(it) },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinListContent(
    uiState: CoinUiState,
    onCoinClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onRefresh: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val pullToRefreshState = rememberPullToRefreshState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CoinPulseColors.Background)
    ) {
        if (uiState.isSearchActive) {
            TopAppBar(
                title = {
                    TextField(
                        value = uiState.searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = {
                            Text(
                                text = "Search coins...",
                                color = CoinPulseColors.TextSecondary
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = CoinPulseColors.TextPrimary,
                            unfocusedTextColor = CoinPulseColors.TextPrimary,
                            focusedContainerColor = CoinPulseColors.Surface,
                            unfocusedContainerColor = CoinPulseColors.Surface,
                            cursorColor = CoinPulseColors.Primary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .padding(end = 8.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onSearchActiveChange(false) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = CoinPulseColors.TextPrimary
                        )
                    }
                },
                actions = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = CoinPulseColors.TextPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CoinPulseColors.Background
                )
            )
        } else {
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
                    IconButton(onClick = { onSearchActiveChange(true) }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = CoinPulseColors.TextPrimary
                        )
                    }
                    IconButton(
                        onClick = onRefresh,
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
        }

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
                        text = uiState.errorMessage,
                        color = CoinPulseColors.PriceDown
                    )
                }
            }
            else -> {
                if (isMobile) {
                    PullToRefreshBox(
                        isRefreshing = uiState.isLoading,
                        onRefresh = onRefresh,
                        state = pullToRefreshState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CoinLazyList(
                            coins = uiState.filteredCoins,
                            favorites = uiState.favorites,
                            currency = uiState.currency,
                            showSummaryCard = !uiState.isSearchActive,
                            onCoinClick = onCoinClick,
                            onFavoriteClick = onFavoriteClick
                        )
                    }
                } else {
                    CoinLazyList(
                        coins = uiState.filteredCoins,
                        favorites = uiState.favorites,
                        currency = uiState.currency,
                        showSummaryCard = !uiState.isSearchActive,
                        onCoinClick = onCoinClick,
                        onFavoriteClick = onFavoriteClick
                    )
                }
            }
        }
    }
}

@Composable
private fun CoinLazyList(
    coins: List<Coin>,
    favorites: Set<String>,
    currency: String,
    showSummaryCard: Boolean,
    onCoinClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit
) {
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (showSummaryCard) {
                item { SummaryCard(coins = coins) }
            }
            items(coins) { coin ->
                CoinItem(
                    coin = coin,
                    isFavorite = favorites.contains(coin.id),
                    currency = currency,
                    onCoinClick = { onCoinClick(coin.id) },
                    onFavoriteClick = { onFavoriteClick(coin.id) }
                )
            }
        }
        CoinScrollbar(
            listState = listState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        )
    }
}

@Preview(showBackground = true, name = "CoinList - Data")
@Composable
fun CoinListContentDataPreview() {
    val dummyCoins = listOf(
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
            low24h = 69000.0
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
            low24h = 2000.0
        )
    )

    CoinPulseTheme {
        CoinListContent(
            uiState = CoinUiState(
                coins = dummyCoins,
                favorites = setOf("bitcoin"),
                lastUpdated = "15:00:00",
                isRefreshEnabled = true
            ),
            onCoinClick = {},
            onFavoriteClick = {},
            onRefresh = {},
            onSearchQueryChange = {},
            onSearchActiveChange = {}
        )
    }
}

@Preview(showBackground = true, name = "CoinList - Search Active")
@Composable
fun CoinListContentSearchPreview() {
    val dummyCoins = listOf(
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
            low24h = 69000.0
        )
    )

    CoinPulseTheme {
        CoinListContent(
            uiState = CoinUiState(
                coins = dummyCoins,
                favorites = emptySet(),
                isSearchActive = true,
                searchQuery = "Bit"
            ),
            onCoinClick = {},
            onFavoriteClick = {},
            onRefresh = {},
            onSearchQueryChange = {},
            onSearchActiveChange = {}
        )
    }
}

@Preview(showBackground = true, name = "CoinList - Loading")
@Composable
fun CoinListContentLoadingPreview() {
    CoinPulseTheme {
        CoinListContent(
            uiState = CoinUiState(isLoading = true),
            onCoinClick = {},
            onFavoriteClick = {},
            onRefresh = {},
            onSearchQueryChange = {},
            onSearchActiveChange = {}
        )
    }
}

@Preview(showBackground = true, name = "CoinList - Error")
@Composable
fun CoinListContentErrorPreview() {
    CoinPulseTheme {
        CoinListContent(
            uiState = CoinUiState(errorMessage = "Rate limit exceeded. Please wait a moment."),
            onCoinClick = {},
            onFavoriteClick = {},
            onRefresh = {},
            onSearchQueryChange = {},
            onSearchActiveChange = {}
        )
    }
}