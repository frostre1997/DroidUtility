package com.frostre1997.droidutility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun DroidUtilityTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
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
            error = Color(0xFFCF6679),
            errorContainer = Color(0xFFB00020),
            onError = Color.Black,
            onErrorContainer = Color.White,
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF1976D2),
            secondary = Color(0xFF00897B),
            tertiary = Color(0xFF388E3C),
            background = Color(0xFFF5F5F5),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFE8E8E8),
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color(0xFF1A1A1A),
            onSurface = Color(0xFF1A1A1A),
            error = Color(0xFFB00020),
            errorContainer = Color(0xFFFFDAD6),
            onError = Color.White,
            onErrorContainer = Color(0xFF410002),
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = Shapes(
            small = RoundedCornerShape(12.dp),
            medium = RoundedCornerShape(18.dp),
            large = RoundedCornerShape(24.dp)
        )
    ) {
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

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
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

// ─── Terminal Tab ──────────────────────────────────────────────────────────────

@Composable
fun TerminalTab() {
    var command by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("Waiting for command...") }
    var isRunning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val shizukuAvailable = remember { ShizukuShellManager.checkAvailability() }
    val hasPermission = remember { ShizukuShellManager.hasPermission() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "DroidUtility • Terminal",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (!shizukuAvailable || !hasPermission) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        if (!shizukuAvailable) "Shizuku is not running" else "Shizuku permission required",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        if (!shizukuAvailable) "Start Shizuku and try again."
                        else "Grant DroidUtility access in Shizuku.",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 13.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = command,
            onValueChange = { command = it },
            label = { Text("Shell command") },
            placeholder = { Text("e.g., pm list packages") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            enabled = !isRunning && hasPermission
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    scope.launch {
                        isRunning = true
                        val result = withContext(Dispatchers.IO) {
                            ShizukuShellManager.executeCommand(command)
                        }
                        output = result.displayText()
                        isRunning = false
                    }
                },
                enabled = command.isNotBlank() && !isRunning && hasPermission
            ) {
                if (isRunning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Execute")
            }

            OutlinedButton(
                onClick = {
                    command = ""
                    output = "Cleared."
                }
            ) {
                Text("Clear")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium
        ) {
            Box(modifier = Modifier.padding(12.dp)) {
                SelectionContainer {
                    Text(
                        text = output,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

// ─── Debloat Tab ──────────────────────────────────────────────────────────────

@Composable
fun DebloatTab() {
    var configs by remember { mutableStateOf<List<Pair<String, DebloatConfig>>>(emptyList()) }
    var selectedConfig by remember { mutableStateOf<DebloatConfig?>(null) }
    var results by remember { mutableStateOf<List<DebloatResult>?>(null) }
    var isRunning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val hasPermission = remember { ShizukuShellManager.hasPermission() }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val configsDir = File("/storage/emulated/0/DroidUtility/configs")
            if (!configsDir.exists()) configsDir.mkdirs()
            configs = DebloatEngine.loadConfigsFromDir(configsDir)
        }
    }

    if (!hasPermission) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    "Shizuku permission required",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    "Grant DroidUtility access in Shizuku to use debloat features.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    } else if (results != null && selectedConfig != null) {
        ResultsView(
            configName = selectedConfig!!.name,
            results = results!!,
            onBack = {
                results = null
                selectedConfig = null
            }
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "DroidUtility • Debloat Manager",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Select a configuration to apply system optimizations.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (configs.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("No configs found", fontWeight = FontWeight.Bold)
                            Text(
                                "Place JSON config files in /storage/emulated/0/DroidUtility/configs/",
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            items(configs) { (_, config) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedConfig = config
                            scope.launch {
                                isRunning = true
                                results = withContext(Dispatchers.IO) {
                                    DebloatEngine.applyConfig(config)
                                }
                                isRunning = false
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(config.name, fontWeight = FontWeight.Bold)
                            Text("${config.packages.size} packages", fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(config.description, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ResultsView(configName: String, results: List<DebloatResult>, onBack: () -> Unit) {
    val successCount = results.count { it.success }
    val failCount = results.count { !it.success }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Back")
            }
        }

        Text(
            "Results: $configName",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "$successCount succeeded, $failCount failed",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(results) { result ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (result.success)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (result.success) Icons.Default.CheckCircle
                            else Icons.Default.Error,
                            contentDescription = null,
                            tint = if (result.success)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                result.packageName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                "${result.action}: ${result.message}",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─── Status Tab ──────────────────────────────────────────────────────────────

@Composable
fun StatusTab() {
    var statusLines by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val commandList = listOf(
        "getprop ro.build.version.release",
        "getprop ro.product.manufacturer",
        "getprop ro.product.model",
        "getprop ro.product.cpu.abi",
        "getprop ro.build.version.sdk",
        "dumpsys battery | grep level",
        "cat /sys/class/power_supply/battery/health",
        "cat /proc/meminfo | grep MemTotal",
        "cat /proc/meminfo | grep MemAvailable",
        "df -h /data | tail -1",
        "uptime"
    )

    // Load data on first composition
    LaunchedEffect(Unit) {
        isLoading = true
        val shizukuAvailable = ShizukuShellManager.checkAvailability()
        val hasPermission = ShizukuShellManager.hasPermission()

        if (!shizukuAvailable || !hasPermission) {
            errorMessage = if (!shizukuAvailable) {
                "Shizuku is not running. Start Shizuku first."
            } else {
                "Shizuku permission not granted. Grant it in Shizuku app."
            }
        } else {
            try {
                val results = ShizukuShellManager.executeCommands(commandList)
                statusLines = results.map { result ->
                    if (result.success && result.output.isNotBlank()) {
                        result.output.trim()
                    } else {
                        "Unknown"
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.localizedMessage ?: e.toString()}"
            }
        }
        isLoading = false
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "DroidUtility • System Status",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }

        if (errorMessage != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage!!,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    isLoading = true
                    errorMessage = null
                    scope.launch {
                        val shizukuAvailable = ShizukuShellManager.checkAvailability()
                        val hasPermission = ShizukuShellManager.hasPermission()

                        if (!shizukuAvailable || !hasPermission) {
                            errorMessage = if (!shizukuAvailable) {
                                "Shizuku is not running. Start Shizuku first."
                            } else {
                                "Shizuku permission not granted. Grant it in Shizuku app."
                            }
                        } else {
                            try {
                                val results = ShizukuShellManager.executeCommands(commandList)
                                statusLines = results.map { result ->
                                    if (result.success && result.output.isNotBlank()) {
                                        result.output.trim()
                                    } else {
                                        "Unknown"
                                    }
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error: ${e.localizedMessage ?: e.toString()}"
                            }
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Retry")
            }
            return@Column
        }

        // No error – show data
        if (statusLines.isNotEmpty()) {
            val labels = listOf(
                "Android Version", "Manufacturer", "Device Model",
                "CPU Architecture", "SDK Level", "Battery Level",
                "Battery Health", "Total RAM", "Available RAM",
                "Storage (data)", "Uptime"
            )

            labels.zip(statusLines).forEach { (label, value) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                        val cleanValue = value
                            .replace("level:", "", ignoreCase = true)
                            .replace("health:", "", ignoreCase = true)
                            .trim()
                        val displayValue = when {
                            cleanValue.contains("good", ignoreCase = true) || cleanValue == "2" -> "✅ Good"
                            cleanValue.contains("cold", ignoreCase = true) || cleanValue == "7" -> "❄️ Cold"
                            cleanValue.contains("overheat", ignoreCase = true) || cleanValue == "3" -> "🔥 Overheat"
                            cleanValue.contains("dead", ignoreCase = true) || cleanValue == "4" -> "💀 Dead"
                            else -> cleanValue
                        }
                        Text(
                            displayValue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = {
                    isLoading = true
                    errorMessage = null
                    scope.launch {
                        val shizukuAvailable = ShizukuShellManager.checkAvailability()
                        val hasPermission = ShizukuShellManager.hasPermission()

                        if (!shizukuAvailable || !hasPermission) {
                            errorMessage = if (!shizukuAvailable) {
                                "Shizuku is not running. Start Shizuku first."
                            } else {
                                "Shizuku permission not granted. Grant it in Shizuku app."
                            }
                        } else {
                            try {
                                val results = ShizukuShellManager.executeCommands(commandList)
                                statusLines = results.map { result ->
                                    if (result.success && result.output.isNotBlank()) {
                                        result.output.trim()
                                    } else {
                                        "Unknown"
                                    }
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error: ${e.localizedMessage ?: e.toString()}"
                            }
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Refresh")
            }
        }
    }
}
