package com.frostre1997.droidutility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DroidUtilityTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun DroidUtilityTheme(content: @Composable () -> Unit) {
    val colorScheme = darkColorScheme(
        primary = Color(0xFF90CAF9),
        secondary = Color(0xFF80CBC4),
        tertiary = Color(0xFFA5D6A7),
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        surfaceVariant = Color(0xFF2C2C2C),
        onPrimary = Color(0xFF0D0D0D),
        onSecondary = Color(0xFF0D0D0D),
        onBackground = Color(0xFFE0E0E0),
        onSurface = Color(0xFFE0E0E0),
    )
    MaterialTheme(colorScheme = colorScheme) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        Triple("Terminal", Icons.Default.Terminal, "Execute shell commands"),
        Triple("Debloat", Icons.Default.DeleteSweep, "Remove bloatware"),
        Triple("Status", Icons.Default.Info, "System information")
    )

    val appBarTitle = when (selectedTab) {
        0 -> "DroidUtility • Terminal"
        1 -> "DroidUtility • Debloat Manager"
        2 -> "DroidUtility • System Status"
        else -> "DroidUtility"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(appBarTitle, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                tabs.forEachIndexed { index, (title, icon, _) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = title) },
                        label = { Text(title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                0 -> TerminalTab()
                1 -> DebloatTab()
                2 -> StatusTab()
            }
        }
    }
}
