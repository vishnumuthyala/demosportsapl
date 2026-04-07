package com.demosportsapl.app.ui.sports

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.demosportsapl.app.ui.common.TopAppBarWithDrawer
import com.demosportsapl.app.ui.navigation.NavRoutes

@Composable
fun SportsScreen(
    navController: NavController,
    drawerState: DrawerState,
    vm: SportsViewModel = viewModel(factory = SportsViewModel.Factory(LocalContext.current))
) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { TopAppBarWithDrawer("Sports", drawerState) }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            items(state.sports) { sport ->
                ListItem(
                    headlineContent = { Text(sport.name, style = MaterialTheme.typography.bodyLarge) },
                    supportingContent = if (sport.description.isNotBlank()) {
                        { Text(sport.description.take(60) + if (sport.description.length > 60) "..." else "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    } else null,
                    trailingContent = { Icon(Icons.Default.ChevronRight, null) },
                    modifier = Modifier.clickable {
                        navController.navigate(NavRoutes.sportDetail(sport.id))
                    }
                )
                Divider(Modifier.padding(start = 16.dp))
            }
        }
    }
}
