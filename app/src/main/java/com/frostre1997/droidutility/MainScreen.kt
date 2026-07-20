package com.frostre1997.droidutility

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.frostre1997.droidutility.navigation.FloatingBottomBar
import com.frostre1997.droidutility.ui.screens.*

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: Screen.Home.route

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content – each screen handles its own background
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // space for floating bar
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Terminal.route) { TerminalScreen() }
            composable(Screen.Debloat.route) { DebloatScreen() }
            composable(Screen.Shell.route) { ShellScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
        }

        // Floating bottom bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            FloatingBottomBar(
                currentRoute = currentRoute,
                onItemClick = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
