package com.frostre1997.droidutility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frostre1997.droidutility.ui.screens.DebloatScreen
import com.frostre1997.droidutility.ui.screens.TerminalScreen
import com.frostre1997.droidutility.ui.theme.DroidUtilityTheme

class DroidUtilityApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DroidUtilityTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "debloat") {
        composable("debloat") {
            DebloatScreen(onBack = { navController.popBackStack() })
        }
        composable("terminal") {
            TerminalScreen(onBack = { navController.popBackStack() })
        }
    }
}
