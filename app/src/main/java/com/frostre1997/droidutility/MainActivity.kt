package com.frostre1997.droidutility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frostre1997.droidutility.navigation.Screen
import com.frostre1997.droidutility.ui.screens.ConsoleScreen
import com.frostre1997.droidutility.ui.screens.DebloatScreen
import com.frostre1997.droidutility.ui.screens.HomeScreen
import com.frostre1997.droidutility.ui.screens.SettingsScreen
import com.frostre1997.droidutility.ui.screens.TerminalScreen
import com.frostre1997.droidutility.ui.theme.DroidUtilityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DroidUtilityTheme {
                Surface(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
                    AppNav()
                }
            }
        }
    }
}

@Composable
private fun AppNav() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onOpenTerminal = { navController.navigate(Screen.Terminal.route) },
                onOpenDebloat = { navController.navigate(Screen.Debloat.route) },
                onOpenConsole = { navController.navigate(Screen.Console.route) },
                onOpenSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.Terminal.route) {
            TerminalScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Debloat.route) {
            DebloatScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Console.route) {
            ConsoleScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
