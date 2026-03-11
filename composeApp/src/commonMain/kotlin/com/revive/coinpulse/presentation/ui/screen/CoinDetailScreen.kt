package com.revive.coinpulse.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.presentation.ui.theme.CoinPulseColors
import com.revive.coinpulse.presentation.ui.theme.CoinPulseTheme
import com.revive.coinpulse.toFormattedPercent
import com.revive.coinpulse.toFormattedPrice

@Composable
fun CoinDetailScreen(
    coin: Coin,
    onBackClick: () -> Unit
) {
    CoinDetailContent(
        coin = coin,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailContent(
    coin: Coin,
    onBackClick: () -> Unit
) {
    val priceChange = coin.priceChangePercentage24h ?: 0.0
    val priceChangeColor = if (priceChange >= 0) CoinPulseColors.PriceUp else CoinPulseColors.PriceDown

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CoinPulseColors.Background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = coin.name,
                    color = CoinPulseColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = CoinPulseColors.TextPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = CoinPulseColors.Background
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    model = coin.imageUrl,
                    contentDescription = coin.name,
                    modifier = Modifier.size(64.dp)
                )
                Column {
                    Text(
                        text = coin.name,
                        color = CoinPulseColors.TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = coin.symbol.uppercase(),
                        color = CoinPulseColors.TextSecondary,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = coin.marketCapRank?.let { "#$it" } ?: "-",
                    color = CoinPulseColors.Primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(
                    text = coin.currentPrice.toFormattedPrice(),
                    color = CoinPulseColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                )
                Text(
                    text = priceChange.toFormattedPercent(),
                    color = priceChangeColor,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CoinPulseColors.Surface)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DetailRow(label = "Market Cap", value = coin.marketCap.toFormattedPrice())
                HorizontalDivider(color = CoinPulseColors.Background)
                DetailRow(label = "24h Volume", value = (coin.totalVolume ?: 0.0).toFormattedPrice())
                HorizontalDivider(color = CoinPulseColors.Background)
                DetailRow(label = "24h High", value = coin.high24h?.toFormattedPrice() ?: "-")
                HorizontalDivider(color = CoinPulseColors.Background)
                DetailRow(label = "24h Low", value = coin.low24h?.toFormattedPrice() ?: "-")
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = CoinPulseColors.TextSecondary,
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = CoinPulseColors.TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true, name = "CoinDetail - Price Up")
@Composable
fun CoinDetailContentUpPreview() {
    CoinPulseTheme {
        CoinDetailContent(
            coin = Coin(
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
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "CoinDetail - Price Down")
@Composable
fun CoinDetailContentDownPreview() {
    CoinPulseTheme {
        CoinDetailContent(
            coin = Coin(
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
            ),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "CoinDetail - Null Fields")
@Composable
fun CoinDetailContentNullPreview() {
    CoinPulseTheme {
        CoinDetailContent(
            coin = Coin(
                id = "unknown",
                symbol = "unk",
                name = "Unknown Coin",
                currentPrice = 0.0,
                priceChangePercentage24h = null,
                marketCap = 0.0,
                totalVolume = 0.0,
                imageUrl = "",
                marketCapRank = null,
                high24h = null,
                low24h = null
            ),
            onBackClick = {}
        )
    }
}