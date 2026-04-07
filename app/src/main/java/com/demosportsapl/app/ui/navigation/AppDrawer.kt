package com.demosportsapl.app.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

data class DrawerItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun AppDrawer(
    navController: NavController,
    drawerState: DrawerState,
    currentRoute: String?
) {
    val scope = rememberCoroutineScope()
    val drawerItems = listOf(
        DrawerItem(NavRoutes.FIXTURES,  "Fixtures",  Icons.Default.DateRange),
        DrawerItem(NavRoutes.RESULTS,   "Results",   Icons.Default.CheckCircle),
        DrawerItem(NavRoutes.STANDINGS, "Standings", Icons.Default.BarChart),
        DrawerItem(NavRoutes.SEARCH,    "Search",    Icons.Default.Search),
        DrawerItem(NavRoutes.RULEBOOK,  "Rulebook",  Icons.Default.Info),
        DrawerItem(NavRoutes.LOGIN,     "Login",     Icons.Default.AccountCircle)
    )

    ModalDrawerSheet {
        Spacer(Modifier.height(16.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "DemosportsAPL",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Divider()
        Spacer(Modifier.height(8.dp))
        drawerItems.forEach { item ->
            NavigationDrawerItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(item.route) {
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}
