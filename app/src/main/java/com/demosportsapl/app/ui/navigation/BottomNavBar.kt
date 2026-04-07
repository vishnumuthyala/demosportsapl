package com.demosportsapl.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(NavRoutes.HOME,     "Home",     Icons.Default.Home),
        BottomNavItem(NavRoutes.SPORTS,   "Sports",   Icons.Default.Star),
        BottomNavItem(NavRoutes.MY_TABLE, "My Table", Icons.Default.Person),
        BottomNavItem(NavRoutes.ALERTS,   "Alerts",   Icons.Default.Notifications)
    )
    val backStack by navController.currentBackStackEntryAsState()
    val current = backStack?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = current == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(NavRoutes.HOME) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
