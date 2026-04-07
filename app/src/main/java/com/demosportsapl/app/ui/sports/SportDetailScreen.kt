package com.demosportsapl.app.ui.sports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.demosportsapl.app.data.local.db.entities.ResultEntity
import com.demosportsapl.app.data.local.db.entities.TableEntity
import com.demosportsapl.app.domain.model.TableStanding
import com.demosportsapl.app.ui.common.TopAppBarWithDrawer
import com.demosportsapl.app.ui.home.LeaderboardHeader
import com.demosportsapl.app.ui.home.LeaderboardRow
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SportDetailScreen(
    sportId: Int,
    navController: NavController,
    drawerState: DrawerState
) {
    val ctx = LocalContext.current
    val vm: SportDetailViewModel = viewModel(
        key = "sport_$sportId",
        factory = SportDetailViewModel.Factory(ctx, sportId)
    )
    val state by vm.state.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Fixtures", "Results", "Standings", "Rulebook")

    Scaffold(
        topBar = {
            TopAppBarWithDrawer(
                title = state.sport?.name ?: "Sport Detail",
                drawerState = drawerState
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { idx, title ->
                    Tab(
                        selected = selectedTab == idx,
                        onClick = { selectedTab = idx },
                        text = { Text(title) }
                    )
                }
            }
            when (selectedTab) {
                0 -> SportFixturesTab(state.fixtures, state.allTables)
                1 -> SportResultsTab(state.fixtures, state.results, state.allTables)
                2 -> SportStandingsTab(state.standings)
                3 -> SportRulebookTab(state.sport?.name ?: "")
            }
        }
    }
}

@Composable
fun SportFixturesTab(fixtures: List<FixtureEntity>, tables: List<TableEntity>) {
    val tableMap = remember(tables) { tables.associateBy { it.id } }
    val sdf = remember { SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()) }
    if (fixtures.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No fixtures yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }
    LazyColumn {
        items(fixtures) { f ->
            Card(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)) {
                Column(Modifier.padding(12.dp)) {
                    Text("${tableMap[f.homeTableId]?.name ?: "?"} vs ${tableMap[f.awayTableId]?.name ?: "?"}",
                        fontWeight = FontWeight.SemiBold)
                    Text(sdf.format(Date(f.scheduledAt)), style = MaterialTheme.typography.bodySmall)
                    Text("Venue: ${f.venue}", style = MaterialTheme.typography.bodySmall)
                    StatusChip(f.status)
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (label, color) = when (status) {
        "LIVE" -> "LIVE" to MaterialTheme.colorScheme.error
        "COMPLETED" -> "Completed" to MaterialTheme.colorScheme.primary
        "WALKOVER" -> "Walk-over" to MaterialTheme.colorScheme.tertiary
        else -> "Scheduled" to MaterialTheme.colorScheme.secondary
    }
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Text(label, Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall, color = color)
    }
}

@Composable
fun SportResultsTab(fixtures: List<FixtureEntity>, results: List<ResultEntity>, tables: List<TableEntity>) {
    val tableMap = remember(tables) { tables.associateBy { it.id } }
    val resultMap = remember(results) { results.associateBy { it.fixtureId } }
    val completed = fixtures.filter { resultMap.containsKey(it.id) }
    if (completed.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No results yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }
    LazyColumn {
        items(completed) { f ->
            val r = resultMap[f.id]!!
            Card(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("${tableMap[f.homeTableId]?.name ?: "?"}", fontWeight = FontWeight.Medium)
                    }
                    Text("${r.homeScore} - ${r.awayScore}", fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium)
                    Column(Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                        Text("${tableMap[f.awayTableId]?.name ?: "?"}", fontWeight = FontWeight.Medium)
                    }
                    if (r.medalType != null) {
                        Spacer(Modifier.width(8.dp))
                        MedalBadge(r.medalType)
                    }
                }
                if (r.isWalkover) {
                    Text("Walk-over", Modifier.padding(start = 12.dp, bottom = 8.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary)
                }
            }
        }
    }
}

@Composable
fun MedalBadge(medal: String) {
    val (label, color) = when (medal) {
        "GOLD"   -> "G" to androidx.compose.ui.graphics.Color(0xFFFFB300)
        "SILVER" -> "S" to androidx.compose.ui.graphics.Color(0xFF9E9E9E)
        "BRONZE" -> "B" to androidx.compose.ui.graphics.Color(0xFFBF8C66)
        else -> return
    }
    Surface(
        shape = MaterialTheme.shapes.small,
        color = color
    ) {
        Text(label, Modifier.padding(4.dp), style = MaterialTheme.typography.labelSmall,
            color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SportStandingsTab(standings: List<TableStanding>) {
    if (standings.all { it.points == 0 }) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No standings yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }
    LazyColumn {
        item { LeaderboardHeader() }
        items(standings) { s -> LeaderboardRow(s) }
    }
}

@Composable
fun SportRulebookTab(sportName: String) {
    LazyColumn(Modifier.padding(16.dp)) {
        item {
            Text("$sportName — Competition Rules",
                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Text(
                "1. Each sport follows standard international rules unless otherwise stated.
" +
                "2. Teams must field a minimum of the required number of players.
" +
                "3. Matches start at the scheduled time. A 10-minute grace period is allowed.
" +
                "4. Failure to appear after the grace period results in a walk-over (3-0).
" +
                "5. Roster must be submitted and locked 60 minutes before kick-off.
" +
                "6. Any substitutions after the lock require Super Admin approval.
" +
                "7. Medal points: Gold = 5 pts, Silver = 3 pts, Bronze = 1 pt.
" +
                "8. Tie-breakers: Total Points > Gold count > Silver count > Bronze count > Alphabetical table name.
" +
                "9. Disputes must be raised within 30 minutes of the result being recorded.
" +
                "10. The decision of the Sport Admin is final.",
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = androidx.compose.ui.unit.sp(22f)
            )
        }
    }
}
