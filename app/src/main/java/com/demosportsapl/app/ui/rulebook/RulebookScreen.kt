package com.demosportsapl.app.ui.rulebook

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.demosportsapl.app.ui.common.TopAppBarWithDrawer

@Composable
fun RulebookScreen(
    navController: NavController,
    drawerState: DrawerState
) {
    Scaffold(
        topBar = { TopAppBarWithDrawer("Rulebook", drawerState) }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RuleSection("Overview",
                "DemosportsAPL is the Annual Premier League for all 25 tables. " +
                "Each table competes across 25 sports. Medals are awarded for top finishers " +
                "in each sport: Gold (1st), Silver (2nd), Bronze (3rd).")

            RuleSection("Points System",
                "Gold Medal  = 5 points
" +
                "Silver Medal = 3 points
" +
                "Bronze Medal = 1 point

" +
                "Tie-breakers (in order):
" +
                "  1. Total points
" +
                "  2. Gold medal count
" +
                "  3. Silver medal count
" +
                "  4. Bronze medal count
" +
                "  5. Alphabetical table name (ascending)")

            RuleSection("Roster Rules",
                "• Each table must submit a roster for each sport at least 60 minutes " +
                "before the scheduled match start time.
" +
                "• After the lock time, no changes are permitted unless overridden by " +
                "the Super Admin with a recorded reason.
" +
                "• Chairman can edit their own table's roster before the lock. " +
                "Offline roster editing is not permitted for Chairmen.")

            RuleSection("Walk-over Rule",
                "• A 10-minute grace period is allowed after the scheduled match start.
" +
                "• If a team fails to appear within the grace period, a walk-over is declared.
" +
                "• Walk-over score is fixed at 3-0.
" +
                "• Only Sport Admins can declare walk-overs.
" +
                "• Walk-overs are recorded in the results and count toward the leaderboard.")

            RuleSection("Roles & Permissions",
                "Super Admin:
" +
                "  • Can override roster locks (reason required, recorded in audit log)
" +
                "  • Full access to all features

" +
                "Sport Admin:
" +
                "  • Can enter scores for fixtures
" +
                "  • Can declare walk-overs
" +
                "  • Can view all fixtures and results

" +
                "Chairman:
" +
                "  • Can view and edit their own table's roster (before lock)
" +
                "  • Cannot edit roster when offline
" +
                "  • Can view all public content

" +
                "General User:
" +
                "  • View-only access to all public content")

            RuleSection("Offline Support",
                "• All read operations (fixtures, results, standings) are available offline " +
                "using local cached data.
" +
                "• Sport Admins can queue score entries offline; they sync when connectivity is restored.
" +
                "• Chairman roster edits are NOT available offline.")

            RuleSection("Disputes",
                "• Any disputes about results must be raised within 30 minutes of the " +
                "result being recorded.
" +
                "• Contact the Sport Admin for your sport immediately.
" +
                "• The Sport Admin's decision is final unless escalated to Super Admin.")
        }
    }
}

@Composable
fun RuleSection(title: String, body: String) {
    Column {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(4.dp))
        Text(body, style = MaterialTheme.typography.bodyMedium, lineHeight = androidx.compose.ui.unit.sp(22f))
        Divider(Modifier.padding(top = 12.dp))
    }
}
