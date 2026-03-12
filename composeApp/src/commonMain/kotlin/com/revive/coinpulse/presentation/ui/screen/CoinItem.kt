package com.revive.coinpulse.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.presentation.ui.theme.CoinPulseColors
import com.revive.coinpulse.toFormattedPercent
import com.revive.coinpulse.toFormattedPrice

@Composable
fun CoinItem(
    coin: Coin,
    isFavorite: Boolean,
    currency: String = "usd",
    onCoinClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    val favoriteColor = if (isFavorite) Color(0xFFFFD700) else CoinPulseColors.TextSecondary

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CoinPulseColors.Surface)
                .clickable { onCoinClick() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f),
        ) {
            AsyncImage(
                model = coin.imageUrl,
                contentDescription = coin.name,
                modifier = Modifier.size(40.dp),
            )
            Column {
                Text(
                    text = coin.name,
                    color = CoinPulseColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = coin.symbol.uppercase(),
                    color = CoinPulseColors.TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = coin.currentPrice.toFormattedPrice(currency),
                    color = CoinPulseColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
                val priceChange = coin.priceChangePercentage24h ?: 0.0
                val priceChangeColor =
                    if (priceChange >= 0) {
                        CoinPulseColors.PriceUp
                    } else {
                        CoinPulseColors.PriceDown
                    }
                Text(
                    text = priceChange.toFormattedPercent(),
                    color = priceChangeColor,
                    fontSize = 12.sp,
                )
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "Favorite",
                    tint = favoriteColor,
                )
            }
        }
    }
}
