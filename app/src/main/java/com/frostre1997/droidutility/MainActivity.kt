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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DroidUtility", fontWeight = FontWeight.Bold) },
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

// Terminal Tab

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
        if (!shizukuAvailable || !hasPermission) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
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
            placeholder = { Text("e.g., pm list packages | grep google") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
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

// Debloat Tab

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
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Shizuku permission required", fontWeight = FontWeight.Bold)
                Text("Grant DroidUtility access in Shizuku to use debloat features.", fontSize = 13.sp)
            }
        }
        return
    }

    if (results != null && selectedConfig != null) {
        ResultsView(
            configName = selectedConfig!!.name,
            results = results!!,
            onBack = {
                results = null
                selectedConfig = null
            }
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Debloat Configurations", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Select a configuration to apply system optimizations.", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (configs.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("No configs found", fontWeight = FontWeight.Bold)
                        Text("Place JSON config files in /storage/emulated/0/DroidUtility/configs/", fontSize = 13.sp)
                    }
                }
            }
        }

        items(configs) { (filename, config) ->
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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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

        Text("Results: $configName", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("$successCount succeeded, $failCount failed", style = MaterialTheme.typography.bodyMedium)
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
                            tint = if (result.success) Color(0xFF4CAF50) else Color(0xFFEF5350)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(result.packageName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("${result.action}: ${result.message}", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

// Status Tab

@Composable
fun StatusTab() {
    var statusLines by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val results = ShizukuShellManager.executeCommands(
                listOf(
                    "getprop ro.build.version.release",
                    "getprop ro.product.manufacturer",
                    "getprop ro.product.model",
                    "getprop ro.product.cpu.abi",
                    "getprop ro.build.version.sdk",
                    "dumpsys battery | grep level | head -1",
                    "dumpsys battery | grep 'health\\|temperature\\|status'",
                    "cat /proc/meminfo | grep MemTotal",
                    "cat /proc/meminfo | grep MemAvailable",
                    "df -h /data | tail -1",
                    "uptime"
                )
            )
            statusLines = results.map { it.displayText() }
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("System Status", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        val labels = listOf(
            "Android Version", "Manufacturer", "Device Model",
            "CPU Architecture", "SDK Level", "Battery Level",
            "Battery Health", "Total RAM", "Available RAM",
            "Storage (data)", "Uptime"
        )

        labels.zip(statusLines).forEach { (label, value) ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, fontFamily = FontFamily.Monospace)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    isLoading = true
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            val results = ShizukuShellManager.executeCommands(listOf(
                                "getprop ro.build.version.release",
                                "getprop ro.product.manufacturer",
                                "getprop ro.product.model",
                                "getprop ro.product.cpu.abi",
                                "getprop ro.build.version.sdk",
                                "dumpsys battery | grep level | head -1",
                                "dumpsys battery | grep 'health\\|temperature\\|status'",
                                "cat /proc/meminfo | grep MemTotal",
                                "cat /proc/meminfo | grep MemAvailable",
                                "df -h /data | tail -1",
                                "uptime"
                            ))
                            statusLines = results.map { it.displayText() }
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Refresh")
            }
        }
    }
}
