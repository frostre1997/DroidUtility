package com.frostre1997.droidutility

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frostre1997.droidutility.ui.screens.DebloatScreen
import com.frostre1997.droidutility.ui.screens.HomeScreen
import com.frostre1997.droidutility.ui.screens.TerminalScreen
import com.frostre1997.droidutility.ui.screens.SettingsScreen
import com.frostre1997.droidutility.ui.screens.ConsoleScreen

@Composable
fun DroidUtilityApp() {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                HomeScreen(
                    onOpenTerminal = { navController.navigate("terminal") },
                    onOpenDebloat = { navController.navigate("debloat") },
                    onOpenConsole = { navController.navigate("console") },
                    onOpenSettings = { navController.navigate("settings") }
                )
            }
            composable("terminal") {
                TerminalScreen(onBack = { navController.popBackStack() })
            }
            composable("debloat") {
                DebloatScreen(onBack = { navController.popBackStack() })
            }
            composable("console") {
                ConsoleScreen(onBack = { navController.popBackStack() })
            }
            composable("settings") {
                SettingsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
