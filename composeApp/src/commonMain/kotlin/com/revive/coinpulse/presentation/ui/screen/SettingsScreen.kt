package com.revive.coinpulse.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revive.coinpulse.data.AppSettings
import com.revive.coinpulse.data.AppTheme
import com.revive.coinpulse.presentation.ui.theme.CoinPulseColors
import com.revive.coinpulse.presentation.ui.theme.CoinPulseTheme
import com.revive.coinpulse.presentation.viewmodel.CoinViewModel
import com.revive.coinpulse.presentation.viewmodel.SettingsUiState

@Composable
fun SettingsScreen(viewModel: CoinViewModel) {
    val settingsUiState by viewModel.settingsUiState.collectAsState()

    SettingsContent(
        settingsUiState = settingsUiState,
        onCurrencyChange = { viewModel.onCurrencyChange(it) },
        onRefreshIntervalChange = { viewModel.onRefreshIntervalChange(it) },
        onCoinCountChange = { viewModel.onCoinCountChange(it) },
        onThemeChange = { viewModel.onThemeChange(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    settingsUiState: SettingsUiState,
    onCurrencyChange: (String) -> Unit,
    onRefreshIntervalChange: (Long) -> Unit,
    onCoinCountChange: (Int) -> Unit,
    onThemeChange: (AppTheme) -> Unit
) {
    var showCurrencySheet by remember { mutableStateOf(false) }
    var showRefreshSheet by remember { mutableStateOf(false) }
    var showCoinCountSheet by remember { mutableStateOf(false) }
    var showThemeSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CoinPulseColors.Background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Settings",
                    color = CoinPulseColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = CoinPulseColors.Background
            )
        )

        SettingsItem(
            title = "Theme",
            value = AppSettings.themeLabel(settingsUiState.theme),
            onClick = { showThemeSheet = true }
        )
        HorizontalDivider(color = CoinPulseColors.Surface)

        SettingsItem(
            title = "Currency",
            value = settingsUiState.currency.uppercase(),
            onClick = { showCurrencySheet = true }
        )
        HorizontalDivider(color = CoinPulseColors.Surface)

        SettingsItem(
            title = "Auto Refresh",
            value = AppSettings.refreshIntervalLabel(settingsUiState.refreshInterval),
            onClick = { showRefreshSheet = true }
        )
        HorizontalDivider(color = CoinPulseColors.Surface)

        SettingsItem(
            title = "Coin Count",
            value = AppSettings.coinCountLabel(settingsUiState.coinCount),
            onClick = { showCoinCountSheet = true }
        )
        HorizontalDivider(color = CoinPulseColors.Surface)

        SettingsItem(
            title = "Version",
            value = "1.0.0",
            onClick = {}
        )
        HorizontalDivider(color = CoinPulseColors.Surface)
    }

    if (showThemeSheet) {
        ModalBottomSheet(
            onDismissRequest = { showThemeSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = CoinPulseColors.Surface
        ) {
            BottomSheetTitle("Select Theme")
            AppSettings.THEMES.forEach { theme ->
                BottomSheetItem(
                    label = AppSettings.themeLabel(theme),
                    isSelected = theme == settingsUiState.theme,
                    onClick = {
                        onThemeChange(theme)
                        showThemeSheet = false
                    }
                )
            }
        }
    }

    if (showCurrencySheet) {
        ModalBottomSheet(
            onDismissRequest = { showCurrencySheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = CoinPulseColors.Surface
        ) {
            BottomSheetTitle("Select Currency")
            AppSettings.CURRENCIES.forEach { currency ->
                BottomSheetItem(
                    label = currency.uppercase(),
                    isSelected = currency == settingsUiState.currency,
                    onClick = {
                        onCurrencyChange(currency)
                        showCurrencySheet = false
                    }
                )
            }
        }
    }

    if (showRefreshSheet) {
        ModalBottomSheet(
            onDismissRequest = { showRefreshSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = CoinPulseColors.Surface
        ) {
            BottomSheetTitle("Auto Refresh Interval")
            AppSettings.REFRESH_INTERVALS.forEach { interval ->
                BottomSheetItem(
                    label = AppSettings.refreshIntervalLabel(interval),
                    isSelected = interval == settingsUiState.refreshInterval,
                    onClick = {
                        onRefreshIntervalChange(interval)
                        showRefreshSheet = false
                    }
                )
            }
        }
    }

    if (showCoinCountSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCoinCountSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = CoinPulseColors.Surface
        ) {
            BottomSheetTitle("Coin Display Count")
            AppSettings.COIN_COUNTS.forEach { count ->
                BottomSheetItem(
                    label = AppSettings.coinCountLabel(count),
                    isSelected = count == settingsUiState.coinCount,
                    onClick = {
                        onCoinCountChange(count)
                        showCoinCountSheet = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = CoinPulseColors.TextPrimary,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = CoinPulseColors.TextSecondary,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun BottomSheetTitle(title: String) {
    Text(
        text = title,
        color = CoinPulseColors.TextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
    )
    HorizontalDivider(color = CoinPulseColors.Background)
}

@Composable
private fun BottomSheetItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = if (isSelected) CoinPulseColors.Primary else CoinPulseColors.TextPrimary,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = CoinPulseColors.Primary
            )
        }
    }
}

@Preview(showBackground = true, name = "Settings")
@Composable
fun SettingsContentPreview() {
    CoinPulseTheme {
        SettingsContent(
            settingsUiState = SettingsUiState(
                currency = "usd",
                refreshInterval = 60L,
                coinCount = 100
            ),
            onCurrencyChange = {},
            onRefreshIntervalChange = {},
            onCoinCountChange = {},
            onThemeChange = {}
        )
    }
}