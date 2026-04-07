package com.demosportsapl.app.ui.fixtures

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
import com.demosportsapl.app.ui.common.TopAppBarWithDrawer
import com.demosportsapl.app.ui.sports.StatusChip
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FixturesScreen(
    navController: NavController,
    drawerState: DrawerState,
    vm: FixturesViewModel = viewModel(factory = FixturesViewModel.Factory(LocalContext.current))
) {
    val state by vm.state.collectAsState()
    val fixtures = remember(state) { vm.filteredFixtures() }
    var showScoreDialog by remember { mutableStateOf<FixtureEntity?>(null) }
    var showWalkOverDialog by remember { mutableStateOf<FixtureEntity?>(null) }
    val isSportAdmin = state.session.role == "SPORT_ADMIN" || state.session.role == "SUPER_ADMIN"

    Scaffold(
        topBar = { TopAppBarWithDrawer("Fixtures", drawerState) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            FilterRow(state, vm)
            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (fixtures.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No fixtures match filter", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                    items(fixtures, key = { it.id }) { f ->
                        FixtureCard(
                            fixture = f,
                            homeTableName = state.tables.find { it.id == f.homeTableId }?.name ?: "?",
                            awayTableName = state.tables.find { it.id == f.awayTableId }?.name ?: "?",
                            sportName = state.sports.find { it.id == f.sportId }?.name ?: "?",
                            result = state.results.find { it.fixtureId == f.id },
                            isSportAdmin = isSportAdmin,
                            onScoreClick = { showScoreDialog = f },
                            onWalkOverClick = { showWalkOverDialog = f }
                        )
                    }
                }
            }
        }
    }

    showScoreDialog?.let { f ->
        ScoreEntryDialog(
            fixture = f,
            homeTableName = state.tables.find { it.id == f.homeTableId }?.name ?: "?",
            awayTableName = state.tables.find { it.id == f.awayTableId }?.name ?: "?",
            onDismiss = { showScoreDialog = null },
            onConfirm = { home, away, medal ->
                vm.enterScore(f.id, home, away, medal)
                showScoreDialog = null
            }
        )
    }

    showWalkOverDialog?.let { f ->
        AlertDialog(
            onDismissRequest = { showWalkOverDialog = null },
            title = { Text("Declare Walk-over?") },
            text = { Text("This will set the score to 3-0 for ${state.tables.find{it.id==f.homeTableId}?.name}. Continue?") },
            confirmButton = {
                TextButton(onClick = { vm.declareWalkOver(f); showWalkOverDialog = null }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showWalkOverDialog = null }) { Text("Cancel") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterRow(state: FixturesUiState, vm: FixturesViewModel) {
    var sportExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }
    Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ExposedDropdownMenuBox(expanded = sportExpanded, onExpandedChange = { sportExpanded = it }, modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                value = state.sports.find { it.id == state.filterSportId }?.name ?: "All Sports",
                onValueChange = {},
                readOnly = true,
                label = { Text("Sport") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(sportExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodySmall
            )
            ExposedDropdownMenu(expanded = sportExpanded, onDismissRequest = { sportExpanded = false }) {
                DropdownMenuItem(text = { Text("All Sports") }, onClick = { vm.setFilterSport(null); sportExpanded = false })
                state.sports.forEach { sport ->
                    DropdownMenuItem(text = { Text(sport.name) }, onClick = { vm.setFilterSport(sport.id); sportExpanded = false })
                }
            }
        }
        ExposedDropdownMenuBox(expanded = statusExpanded, onExpandedChange = { statusExpanded = it }, modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                value = state.filterStatus ?: "All",
                onValueChange = {},
                readOnly = true,
                label = { Text("Status") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(statusExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodySmall
            )
            ExposedDropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) {
                listOf(null, "SCHEDULED", "LIVE", "COMPLETED", "WALKOVER").forEach { s ->
                    DropdownMenuItem(text = { Text(s ?: "All") }, onClick = { vm.setFilterStatus(s); statusExpanded = false })
                }
            }
        }
    }
}

@Composable
fun FixtureCard(
    fixture: FixtureEntity,
    homeTableName: String,
    awayTableName: String,
    sportName: String,
    result: com.demosportsapl.app.data.local.db.entities.ResultEntity?,
    isSportAdmin: Boolean,
    onScoreClick: () -> Unit,
    onWalkOverClick: () -> Unit
) {
    val sdf = remember { SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()) }
    Card(
        Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(sportName, style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(homeTableName, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                if (result != null) {
                    Text("${result.homeScore} - ${result.awayScore}",
                        style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                } else {
                    Text("vs", style = MaterialTheme.typography.bodyMedium)
                }
                Text(awayTableName, Modifier.weight(1f), fontWeight = FontWeight.SemiBold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.End)
            }
            Spacer(Modifier.height(4.dp))
            Text(sdf.format(Date(fixture.scheduledAt)), style = MaterialTheme.typography.bodySmall)
            if (fixture.venue.isNotBlank())
                Text("📍 ${fixture.venue}", style = MaterialTheme.typography.bodySmall)
            StatusChip(fixture.status)
            if (isSportAdmin && fixture.status in listOf("SCHEDULED", "LIVE")) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(onClick = onScoreClick, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Edit, null, Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Score", style = MaterialTheme.typography.labelMedium)
                    }
                    OutlinedButton(onClick = onWalkOverClick, modifier = Modifier.weight(1f)) {
                        Text("Walk-over", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreEntryDialog(
    fixture: FixtureEntity,
    homeTableName: String,
    awayTableName: String,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, String?) -> Unit
) {
    var homeScore by remember { mutableStateOf("") }
    var awayScore by remember { mutableStateOf("") }
    var medal by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enter Score") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("$homeTableName vs $awayTableName", fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = homeScore,
                        onValueChange = { homeScore = it.filter(Char::isDigit) },
                        label = { Text(homeTableName) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        )
                    )
                    OutlinedTextField(
                        value = awayScore,
                        onValueChange = { awayScore = it.filter(Char::isDigit) },
                        label = { Text(awayTableName) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        )
                    )
                }
                Text("Medal (optional)", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("GOLD", "SILVER", "BRONZE", "None").forEach { m ->
                        FilterChip(
                            selected = if (m == "None") medal.isEmpty() else medal == m,
                            onClick = { medal = if (m == "None") "" else m },
                            label = { Text(m) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val h = homeScore.toIntOrNull() ?: return@TextButton
                val a = awayScore.toIntOrNull() ?: return@TextButton
                onConfirm(h, a, medal.ifBlank { null })
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
