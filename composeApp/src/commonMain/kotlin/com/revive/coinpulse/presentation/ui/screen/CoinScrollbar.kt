package com.revive.coinpulse.presentation.ui.screen

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun CoinScrollbar(
    listState: LazyListState,
    modifier: Modifier,
)
