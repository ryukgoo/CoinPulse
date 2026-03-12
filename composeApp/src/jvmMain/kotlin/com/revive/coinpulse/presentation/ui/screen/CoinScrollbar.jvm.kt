package com.revive.coinpulse.presentation.ui.screen

import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.revive.coinpulse.presentation.ui.theme.CoinPulseColors

@Composable
actual fun CoinScrollbar(
    listState: LazyListState,
    modifier: Modifier,
) {
    VerticalScrollbar(
        modifier = modifier,
        adapter = rememberScrollbarAdapter(listState),
        style =
            ScrollbarStyle(
                minimalHeight = 16.dp,
                thickness = 8.dp,
                shape = RoundedCornerShape(4.dp),
                hoverDurationMillis = 300,
                unhoverColor = CoinPulseColors.TextSecondary.copy(alpha = 0.4f),
                hoverColor = CoinPulseColors.TextSecondary,
            ),
    )
}
