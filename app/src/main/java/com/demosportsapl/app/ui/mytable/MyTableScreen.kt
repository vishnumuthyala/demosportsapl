package com.demosportsapl.app.ui.mytable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.demosportsapl.app.data.local.db.entities.RosterEntry
import com.demosportsapl.app.ui.common.TopAppBarWithDrawer
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MyTableScreen(
    navController: NavController,
    drawerState: DrawerState,
    vm: MyTableViewModel = viewModel(factory = MyTableViewModel.Factory(LocalContext.current))
) {
    val state by vm.state.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBarWithDrawer(
                title = "My Table — ${state.session.tableName}",
                drawerState = drawerState
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 },
                    text = { Text("Schedule") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 },
                    text = { Text("Roster") })
            }
            when (selectedTab) {
                0 -> ScheduleTab(state, vm)
                1 -> RosterTab(state, vm)
            }
        }
    }
}

@Composable
fun ScheduleTab(state: MyTableUiState, vm: MyTableViewModel) {
    val sdf = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }
    val tableMap = remember(state.tables) { state.tables.associateBy { it.id } }
    val sportMap = remember(state.sports) { state.sports.associateBy { it.id } }
    val resultMap = remember(state.results) { state.results.associateBy { it.fixtureId } }

    if (state.fixtures.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No fixtures scheduled", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }
    LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
        items(state.fixtures) { f ->
            val result = resultMap[f.id]
            Card(
                Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(sportMap[f.sportId]?.name ?: "Unknown Sport",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "${tableMap[f.homeTableId]?.name ?: "?"} vs ${tableMap[f.awayTableId]?.name ?: "?"}",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(sdf.format(Date(f.scheduledAt)),
                        style = MaterialTheme.typography.bodySmall)
                    if (f.venue.isNotBlank())
                        Text("📍 ${f.venue}", style = MaterialTheme.typography.bodySmall)
                    if (result != null) {
                        Text("Score: ${result.homeScore} - ${result.awayScore}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold)
                    }
                    val locked = vm.isRosterLocked(f.scheduledAt)
                    if (locked) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, null,
                                Modifier.size(14.dp), tint = MaterialTheme.colorScheme.error)
                            Spacer(Modifier.width(4.dp))
                            Text("Roster locked", style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RosterTab(state: MyTableUiState, vm: MyTableViewModel) {
    val isChairman = state.session.role == "CHAIRMAN"
    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newRole by remember { mutableStateOf("Player") }
    var selectedSportId by remember { mutableStateOf(8) }

    Column(Modifier.fillMaxSize()) {
        if (isChairman) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ElevatedButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Add Player")
                }
            }
        }
        if (state.roster.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No roster entries", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn {
                items(state.roster, key = { it.id }) { entry ->
                    RosterEntryRow(
                        entry = entry,
                        sportName = state.sports.find { it.id == entry.sportId }?.name ?: "?",
                        canEdit = isChairman,
                        onDelete = { vm.removePlayer(entry) }
                    )
                    Divider()
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Player") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = newName, onValueChange = { newName = it },
                        label = { Text("Player Name") }, singleLine = true)
                    OutlinedTextField(value = newRole, onValueChange = { newRole = it },
                        label = { Text("Role") }, singleLine = true)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newName.isNotBlank()) {
                        vm.addPlayer(newName.trim(), newRole.trim().ifBlank { "Player" }, selectedSportId)
                        newName = ""; newRole = "Player"; showAddDialog = false
                    }
                }) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun RosterEntryRow(entry: RosterEntry, sportName: String, canEdit: Boolean, onDelete: () -> Unit) {
    ListItem(
        headlineContent = { Text(entry.playerName) },
        supportingContent = { Text("$sportName • ${entry.playerRole}") },
        trailingContent = if (canEdit) {
            { IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Remove", tint = MaterialTheme.colorScheme.error) } }
        } else null
    )
}
