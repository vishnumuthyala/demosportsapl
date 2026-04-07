package com.demosportsapl.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.demosportsapl.app.domain.model.FixtureWithDetails
import com.demosportsapl.app.domain.model.TableStanding
import com.demosportsapl.app.ui.common.TopAppBarWithDrawer
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController,
    drawerState: DrawerState,
    vm: HomeViewModel = viewModel(factory = HomeViewModel.Factory(LocalContext.current))
) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { TopAppBarWithDrawer("DemosportsAPL", drawerState) }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                SectionHeader("Live & Next Matches")
            }
            if (state.liveAndNext.isEmpty()) {
                item { EmptyMessage("No live or upcoming matches today") }
            } else {
                items(state.liveAndNext) { fd ->
                    LiveNextCard(fd)
                }
            }
            item {
                SectionHeader("Global Leaderboard")
            }
            item {
                LeaderboardHeader()
            }
            items(state.standings) { standing ->
                LeaderboardRow(standing)
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    )
}

@Composable
fun EmptyMessage(msg: String) {
    Text(
        text = msg,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun LiveNextCard(fd: FixtureWithDetails) {
    val sdf = remember { SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(fd.sportName, style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary)
                Text("${fd.homeTableName} vs ${fd.awayTableName}",
                    style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                Text(sdf.format(Date(fd.fixture.scheduledAt)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (fd.fixture.venue.isNotBlank()) {
                    Text(fd.fixture.venue, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (fd.fixture.status == "LIVE") {
                Surface(
                    color = Color(0xFFE53935),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text("LIVE", Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White, style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold)
                }
            }
            if (fd.result != null) {
                Spacer(Modifier.width(8.dp))
                Text("${fd.result.homeScore} - ${fd.result.awayScore}",
                    style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun LeaderboardHeader() {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("#",   Modifier.width(28.dp), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text("Table", Modifier.weight(1f), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text("G",   Modifier.width(28.dp), color = Color(0xFFFFD600), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
        Text("S",   Modifier.width(28.dp), color = Color(0xFFBDBDBD), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
        Text("B",   Modifier.width(28.dp), color = Color(0xFFBF8C66), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
        Text("Pts", Modifier.width(36.dp), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.End)
    }
}

@Composable
fun LeaderboardRow(s: TableStanding) {
    val bg = if (s.rank % 2 == 0) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
             else MaterialTheme.colorScheme.surface
    Row(
        Modifier
            .fillMaxWidth()
            .background(bg)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("${s.rank}", Modifier.width(28.dp), fontSize = 12.sp,
            color = if (s.rank <= 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (s.rank <= 3) FontWeight.Bold else FontWeight.Normal)
        Text(s.tableName, Modifier.weight(1f), fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Text("${s.goldCount}",   Modifier.width(28.dp), fontSize = 12.sp, textAlign = TextAlign.Center, color = Color(0xFFFFB300))
        Text("${s.silverCount}", Modifier.width(28.dp), fontSize = 12.sp, textAlign = TextAlign.Center, color = Color(0xFF9E9E9E))
        Text("${s.bronzeCount}", Modifier.width(28.dp), fontSize = 12.sp, textAlign = TextAlign.Center, color = Color(0xFFBF8C66))
        Text("${s.points}",      Modifier.width(36.dp), fontSize = 12.sp, textAlign = TextAlign.End, fontWeight = FontWeight.Bold)
    }
}
