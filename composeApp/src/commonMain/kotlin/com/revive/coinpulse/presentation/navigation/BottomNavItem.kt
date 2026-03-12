package com.revive.coinpulse.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    Home("home", "Home", Icons.Default.Home),
    Favorites("favorites", "Favorites", Icons.Default.Favorite),
    Settings("settings", "Settings", Icons.Default.Settings),
}
