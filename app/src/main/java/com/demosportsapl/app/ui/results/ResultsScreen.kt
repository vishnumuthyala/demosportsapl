package com.demosportsapl.app.ui.results

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.demosportsapl.app.ui.common.TopAppBarWithDrawer
import com.demosportsapl.app.ui.sports.MedalBadge
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ResultsScreen(
    navController: NavController,
    drawerState: DrawerState,
    vm: ResultsViewModel = viewModel(factory = ResultsViewModel.Factory(LocalContext.current))
) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { TopAppBarWithDrawer("Results", drawerState) }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }
        if (state.fixtures.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No results yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            return@Scaffold
        }
        val sdf = remember { SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()) }
        val tableMap = remember(state.tables) { state.tables.associateBy { it.id } }
        val sportMap  = remember(state.sports) { state.sports.associateBy  { it.id } }
        val resultMap = remember(state.results){ state.results.associateBy { it.fixtureId } }

        LazyColumn(Modifier.padding(padding), contentPadding = PaddingValues(bottom = 16.dp)) {
            items(state.fixtures.sortedByDescending { it.scheduledAt }, key = { it.id }) { f ->
                val r = resultMap[f.id] ?: return@items
                Card(
                    Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(sportMap[f.sportId]?.name ?: "?",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary)
                            Text(sdf.format(Date(f.scheduledAt)),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(Modifier.height(4.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(tableMap[f.homeTableId]?.name ?: "?",
                                Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                            Text("${r.homeScore}  -  ${r.awayScore}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center)
                            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(tableMap[f.awayTableId]?.name ?: "?",
                                    fontWeight = FontWeight.SemiBold)
                                if (r.medalType != null) {
                                    Spacer(Modifier.width(6.dp))
                                    MedalBadge(r.medalType)
                                }
                            }
                        }
                        if (r.isWalkover) {
                            Text("Walk-over", style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.tertiary)
                        }
                    }
                }
            }
        }
    }
}
