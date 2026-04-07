package com.demosportsapl.app.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.demosportsapl.app.ui.common.TopAppBarWithDrawer
import com.demosportsapl.app.ui.navigation.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    drawerState: DrawerState,
    vm: LoginViewModel = viewModel(factory = LoginViewModel.Factory(LocalContext.current))
) {
    val state by vm.state.collectAsState()
    val roles = listOf("SUPER_ADMIN", "SPORT_ADMIN", "CHAIRMAN", "GENERAL")
    val roleLabels = mapOf(
        "SUPER_ADMIN" to "Super Admin",
        "SPORT_ADMIN" to "Sport Admin",
        "CHAIRMAN" to "Chairman",
        "GENERAL" to "General User"
    )
    val needsTable = state.selectedRole in listOf("CHAIRMAN", "GENERAL")
    var tableExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(state.loginSuccess) {
        if (state.loginSuccess) {
            navController.navigate(NavRoutes.HOME) { popUpTo(0) { inclusive = true } }
        }
    }

    Scaffold(
        topBar = { TopAppBarWithDrawer("Login", drawerState) }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Select Your Role", style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold)

            roles.forEach { role ->
                OutlinedCard(
                    onClick = { vm.selectRole(role) },
                    border = CardDefaults.outlinedCardBorder().let {
                        if (state.selectedRole == role)
                            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                        else it
                    }
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(roleLabels[role] ?: role, fontWeight = FontWeight.SemiBold)
                            Text(
                                when (role) {
                                    "SUPER_ADMIN" -> "Full access, override capabilities"
                                    "SPORT_ADMIN" -> "Score entry, walk-over management"
                                    "CHAIRMAN" -> "Manage your table's roster"
                                    else -> "View all content"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (state.selectedRole == role) {
                            Icon(Icons.Default.CheckCircle, null,
                                tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            if (needsTable) {
                Text("Select Your Table", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                ExposedDropdownMenuBox(
                    expanded = tableExpanded,
                    onExpandedChange = { tableExpanded = it }
                ) {
                    OutlinedTextField(
                        value = state.selectedTableName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Table") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(tableExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = tableExpanded,
                        onDismissRequest = { tableExpanded = false }
                    ) {
                        state.tables.forEach { table ->
                            DropdownMenuItem(
                                text = { Text(table.name) },
                                onClick = {
                                    vm.selectTable(table.id, table.name)
                                    tableExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { vm.login() },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Login as ${roleLabels[state.selectedRole] ?: state.selectedRole}",
                    fontWeight = FontWeight.Bold)
            }

            val current = state.currentSession
            if (current.role != "GENERAL" || current.tableName.isNotBlank()) {
                Divider()
                Text("Current Session", style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Role: ${roleLabels[current.role] ?: current.role}")
                if (current.role in listOf("CHAIRMAN", "GENERAL")) {
                    Text("Table: ${current.tableName}")
                }
                TextButton(onClick = { vm.logout() }) { Text("Logout") }
            }
        }
    }
}
