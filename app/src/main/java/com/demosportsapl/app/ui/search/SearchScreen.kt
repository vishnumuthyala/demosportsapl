package com.demosportsapl.app.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.demosportsapl.app.data.local.db.entities.FixtureEntity
import com.demosportsapl.app.ui.common.TopAppBarWithDrawer
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SearchScreen(
    navController: NavController,
    drawerState: DrawerState,
    vm: SearchViewModel = viewModel(factory = SearchViewModelFactory(LocalContext.current))
) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { TopAppBarWithDrawer("Search", drawerState) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = state.query,
                onValueChange = { vm.search(it) },
                label = { Text("Search by table number or name") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(16.dp))

            when {
                state.noMatch -> {
                    Text("No table found for "${state.query}"",
                        color = MaterialTheme.colorScheme.error)
                }
                state.matchedTable != null -> {
                    Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text(state.matchedTable!!.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Today's Matches", style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    if (state.fixtures.isEmpty()) {
                        Text("No matches scheduled for today",
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        LazyColumn {
                            items(state.fixtures) { f ->
                                SearchFixtureItem(f, state)
                            }
                        }
                    }
                }
                state.query.isBlank() -> {
                    Text("Enter a table number (1-25) or table name to search.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun SearchFixtureItem(f: FixtureEntity, state: SearchUiState) {
    val sdf = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val tableMap = remember(state.tables) { state.tables.associateBy { it.id } }
    val sportMap  = remember(state.sports) { state.sports.associateBy  { it.id } }
    val resultMap = remember(state.results){ state.results.associateBy { it.fixtureId } }

    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(sportMap[f.sportId]?.name ?: "?",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary)
                Text("${tableMap[f.homeTableId]?.name ?: "?"} vs ${tableMap[f.awayTableId]?.name ?: "?"}",
                    fontWeight = FontWeight.SemiBold)
                Text("⏰ ${sdf.format(Date(f.scheduledAt))} • ${f.venue}",
                    style = MaterialTheme.typography.bodySmall)
            }
            resultMap[f.id]?.let { r ->
                Text("${r.homeScore}-${r.awayScore}", fontWeight = FontWeight.Bold)
            }
        }
    }
}
