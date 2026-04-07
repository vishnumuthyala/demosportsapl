package com.demosportsapl.app.ui.standings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.demosportsapl.app.ui.common.TopAppBarWithDrawer
import com.demosportsapl.app.ui.home.LeaderboardHeader
import com.demosportsapl.app.ui.home.LeaderboardRow

@Composable
fun StandingsScreen(
    navController: NavController,
    drawerState: DrawerState,
    vm: StandingsViewModel = viewModel(factory = StandingsViewModel.Factory(LocalContext.current))
) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { TopAppBarWithDrawer("Standings", drawerState) }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            item { LeaderboardHeader() }
            items(state.standings) { s -> LeaderboardRow(s) }
        }
    }
}
