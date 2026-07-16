package com.frostre1997.droidutility.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.frostre1997.droidutility.tabs.ConsoleTab
import com.frostre1997.droidutility.tabs.HomeTab

object Routes {
    const val HOME = "home"
    const val CONSOLE = "console"
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeTab(
                onOpenConsole = { navController.navigate(Routes.CONSOLE) }
            )
        }
        composable(Routes.CONSOLE) {
            ConsoleTab(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
