package com.frostre1997.droidutility

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Terminal : Screen("terminal", "Terminal", Icons.Default.Terminal)
    object Debloat : Screen("debloat", "Debloat", Icons.Default.Build)
    object Shell : Screen("shell", "Shell", Icons.Default.Code)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}
