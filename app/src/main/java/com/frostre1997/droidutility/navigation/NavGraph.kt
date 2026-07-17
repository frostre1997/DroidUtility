package com.frostre1997.droidutility.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frostre1997.droidutility.ui.screens.DebloatScreen
import com.frostre1997.droidutility.ui.screens.TerminalScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "debloat") {
        composable("debloat") { DebloatScreen() }
        composable("terminal") { TerminalScreen() }
        // add other routes
    }
}
