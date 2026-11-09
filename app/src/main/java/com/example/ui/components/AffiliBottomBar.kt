package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.viewmodel.AppNavScreen

data class NavItem(
    val screen: AppNavScreen,
    val titleEn: String,
    val titleFr: String,
    val titleAr: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector,
    val titleZh: String = ""
)

@Composable
fun AffiliBottomBar(
    currentScreen: AppNavScreen,
    currentLanguage: AppLanguage,
    onNavigate: (AppNavScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(
            screen = AppNavScreen.HOME,
            titleEn = "Deals",
            titleFr = "Bons Plans",
            titleAr = "العروض",
            activeIcon = Icons.Filled.Home,
            inactiveIcon = Icons.Outlined.Home,
            titleZh = "特惠"
        ),
        NavItem(
            screen = AppNavScreen.CATALOG,
            titleEn = "Categories",
            titleFr = "Rayons",
            titleAr = "الأقسام",
            activeIcon = Icons.Filled.Category,
            inactiveIcon = Icons.Outlined.Category,
            titleZh = "分类"
        ),
        NavItem(
            screen = AppNavScreen.AI_ASSISTANT,
            titleEn = "AI Concierge",
            titleFr = "IA Shopping",
            titleAr = "المساعد الذكي",
            activeIcon = Icons.Filled.AutoAwesome,
            inactiveIcon = Icons.Outlined.AutoAwesome,
            titleZh = "AI导购"
        ),
        NavItem(
            screen = AppNavScreen.COMPARE,
            titleEn = "Compare",
            titleFr = "Comparer",
            titleAr = "المقارنة",
            activeIcon = Icons.Filled.CompareArrows,
            inactiveIcon = Icons.Outlined.CompareArrows,
            titleZh = "对比"
        ),
        NavItem(
            screen = AppNavScreen.ADMIN,
            titleEn = "Admin Hub",
            titleFr = "Gestion",
            titleAr = "الإدارة",
            activeIcon = Icons.Filled.AdminPanelSettings,
            inactiveIcon = Icons.Outlined.AdminPanelSettings,
            titleZh = "管理"
        )
    )

    NavigationBar(
        modifier = modifier.navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        items.forEach { item ->
            val isSelected = currentScreen == item.screen
            val label = when (currentLanguage) {
                AppLanguage.EN -> item.titleEn
                AppLanguage.FR -> item.titleFr
                AppLanguage.AR -> item.titleAr
                AppLanguage.ZH -> if (item.titleZh.isNotBlank()) item.titleZh else item.titleEn
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.activeIcon else item.inactiveIcon,
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_${item.screen.name.lowercase()}")
            )
        }
    }
}
