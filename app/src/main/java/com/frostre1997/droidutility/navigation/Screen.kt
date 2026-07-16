package com.frostre1997.droidutility.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Terminal : Screen("terminal")
    data object Debloat : Screen("debloat")
    data object Console : Screen("console")
    data object Settings : Screen("settings")
}
