package com.demosportsapl.app.ui.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.demosportsapl.app.ui.alerts.AlertsScreen
import com.demosportsapl.app.ui.fixtures.FixturesScreen
import com.demosportsapl.app.ui.home.HomeScreen
import com.demosportsapl.app.ui.login.LoginScreen
import com.demosportsapl.app.ui.mytable.MyTableScreen
import com.demosportsapl.app.ui.results.ResultsScreen
import com.demosportsapl.app.ui.rulebook.RulebookScreen
import com.demosportsapl.app.ui.search.SearchScreen
import com.demosportsapl.app.ui.sports.SportDetailScreen
import com.demosportsapl.app.ui.sports.SportsScreen
import com.demosportsapl.app.ui.standings.StandingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    val showBottomBar = currentRoute in listOf(
        NavRoutes.HOME, NavRoutes.SPORTS, NavRoutes.MY_TABLE, NavRoutes.ALERTS
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                navController = navController,
                drawerState = drawerState,
                currentRoute = currentRoute
            )
        }
    ) {
        Scaffold(
            bottomBar = {
                if (showBottomBar) BottomNavBar(navController)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = NavRoutes.HOME,
                modifier = androidx.compose.ui.Modifier.padding(innerPadding)
            ) {
                composable(NavRoutes.HOME) {
                    HomeScreen(navController = navController, drawerState = drawerState)
                }
                composable(NavRoutes.SPORTS) {
                    SportsScreen(navController = navController, drawerState = drawerState)
                }
                composable(NavRoutes.SPORT_DETAIL) { back ->
                    val sportId = back.arguments?.getString("sportId")?.toIntOrNull() ?: 1
                    SportDetailScreen(
                        sportId = sportId,
                        navController = navController,
                        drawerState = drawerState
                    )
                }
                composable(NavRoutes.MY_TABLE) {
                    MyTableScreen(navController = navController, drawerState = drawerState)
                }
                composable(NavRoutes.ALERTS) {
                    AlertsScreen(navController = navController, drawerState = drawerState)
                }
                composable(NavRoutes.FIXTURES) {
                    FixturesScreen(navController = navController, drawerState = drawerState)
                }
                composable(NavRoutes.RESULTS) {
                    ResultsScreen(navController = navController, drawerState = drawerState)
                }
                composable(NavRoutes.STANDINGS) {
                    StandingsScreen(navController = navController, drawerState = drawerState)
                }
                composable(NavRoutes.SEARCH) {
                    SearchScreen(navController = navController, drawerState = drawerState)
                }
                composable(NavRoutes.RULEBOOK) {
                    RulebookScreen(navController = navController, drawerState = drawerState)
                }
                composable(NavRoutes.LOGIN) {
                    LoginScreen(navController = navController, drawerState = drawerState)
                }
            }
        }
    }
}
