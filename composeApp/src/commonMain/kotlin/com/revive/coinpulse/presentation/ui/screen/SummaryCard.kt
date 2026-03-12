package com.revive.coinpulse.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revive.coinpulse.data.model.Coin
import com.revive.coinpulse.presentation.ui.theme.CoinPulseColors

@Composable
fun SummaryCard(coins: List<Coin>) {
    val upCount = coins.count { (it.priceChangePercentage24h ?: 0.0) >= 0 }
    val downCount = coins.size - upCount

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CoinPulseColors.Surface)
                .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        SummaryItem(
            label = "Total",
            value = "${coins.size}",
            color = Color.White,
        )
        SummaryItem(
            label = "Up",
            value = "$upCount",
            color = CoinPulseColors.PriceUp,
        )
        SummaryItem(
            label = "Down",
            value = "$downCount",
            color = CoinPulseColors.PriceDown,
        )
    }
}

@Composable
private fun SummaryItem(
    label: String,
    value: String,
    color: Color,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
        Text(
            text = label,
            color = CoinPulseColors.TextSecondary,
            fontSize = 12.sp,
        )
    }
}
