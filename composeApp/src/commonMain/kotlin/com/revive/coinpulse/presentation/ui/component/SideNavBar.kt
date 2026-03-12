package com.revive.coinpulse.presentation.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revive.coinpulse.presentation.navigation.BottomNavItem
import com.revive.coinpulse.presentation.ui.theme.CoinPulseColors

@Composable
fun SideNavBar(
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit,
    isExpanded: Boolean = true,
    onToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(if (isExpanded) 240.dp else 72.dp)
            .animateContentSize()
            .background(CoinPulseColors.Surface)
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // 헤더 (타이틀 + 토글 버튼)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isExpanded) {
                Text(
                    text = "CoinPulse",
                    color = CoinPulseColors.Primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
            }
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (isExpanded)
                        Icons.AutoMirrored.Filled.MenuOpen
                    else
                        Icons.Default.Menu,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = CoinPulseColors.TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 네비게이션 아이템
        BottomNavItem.entries.forEach { item ->
            val isSelected = currentRoute?.contains(item.route) == true
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item) }
                    .background(
                        if (isSelected) CoinPulseColors.Background
                        else CoinPulseColors.Surface
                    )
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (isSelected) CoinPulseColors.Primary
                    else CoinPulseColors.TextSecondary
                )
                if (isExpanded) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = item.label,
                        color = if (isSelected) CoinPulseColors.Primary
                        else CoinPulseColors.TextSecondary,
                        fontSize = 15.sp,
                        fontWeight = if (isSelected) FontWeight.Bold
                        else FontWeight.Normal
                    )
                }
            }
        }
    }
}