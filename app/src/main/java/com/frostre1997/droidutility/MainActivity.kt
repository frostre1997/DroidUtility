package com.frostre1997.droidutility

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val themeMode = remember { mutableStateOf(ThemePreferences.getThemeMode(context)) }

            DroidUtilityTheme(themeMode = themeMode.value) {
                MainScreen(themeMode = themeMode)
            }
        }
    }
}

@Composable
fun DroidUtilityTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (themeMode) {
        ThemeMode.DARK, ThemeMode.AMOLED -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> systemDark
    }

    val colorScheme = when {
        themeMode == ThemeMode.AMOLED -> darkColorScheme(
            primary = Color(0xFF90CAF9),
            secondary = Color(0xFF80CBC4),
            tertiary = Color(0xFFA5D6A7),
            background = Color.Black,
            surface = Color.Black,
            surfaceVariant = Color(0xFF0A0A0A),
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color(0xFFE0E0E0),
            onSurface = Color(0xFFE0E0E0),
            error = Color(0xFFCF6679),
            errorContainer = Color(0xFFB00020),
            onError = Color.Black,
            onErrorContainer = Color.White,
        )
        darkTheme -> darkColorScheme(
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
        else -> lightColorScheme(
            primary = Color(0xFF1976D2),
            secondary = Color(0xFF00897B),
            tertiary = Color(0xFF388E3C),
            background = Color(0xFFF5F5F5),
            surface = Color.White,
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
fun MainScreen(themeMode: MutableState<ThemeMode>) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        Triple("Terminal", Icons.Default.Terminal, "Execute shell commands"),
        Triple("Debloat", Icons.Default.DeleteSweep, "Remove bloatware"),
        Triple("Status", Icons.Default.Info, "System information"),
        Triple("Settings", Icons.Default.Settings, "App settings")
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
                1 -> DebloatTabNew()
                2 -> StatusTab()
                3 -> SettingsTab(themeMode)
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// TERMINAL TAB - Full Shell Access
// ═══════════════════════════════════════════════════════════════════════════════

data class CommandHistory(val command: String, val output: String, val success: Boolean)

@Composable
fun TerminalTab() {
    var command by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("DroidUtility Terminal v1.0\nType 'help' for available commands.\n\n") }
    var isRunning by remember { mutableStateOf(false) }
    var commandHistory by remember { mutableStateOf<List<CommandHistory>>(emptyList()) }
    var historyIndex by remember { mutableIntStateOf(-1) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val shizukuAvailable = remember { ShizukuShellManager.checkAvailability() }
    val hasPermission = remember { ShizukuShellManager.hasPermission() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Text(
            text = "Terminal",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
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

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium
        ) {
            Box(modifier = Modifier.padding(8.dp)) {
                SelectionContainer {
                    Text(
                        text = output,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = command,
            onValueChange = { command = it },
            label = { Text("Command") },
            placeholder = { Text("e.g., pm list packages") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            enabled = !isRunning && hasPermission,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(
                onGo = {
                    if (command.isNotBlank() && hasPermission) {
                        scope.launch {
                            isRunning = true
                            val result = withContext(Dispatchers.IO) {
                                ShizukuShellManager.executeCommand(command)
                            }
                            output += "\n$ ${command}\n${result.displayText()}\n"
                            commandHistory = commandHistory + CommandHistory(command, result.displayText(), result.success)
                            historyIndex = commandHistory.size
                            command = ""
                            isRunning = false
                        }
                    }
                }
            ),
            trailingIcon = {
                if (command.isNotEmpty()) {
                    IconButton(onClick = { command = "" }) {
                        Icon(Icons.Default.Clear, "Clear")
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (command.isNotBlank() && hasPermission) {
                        scope.launch {
                            isRunning = true
                            val result = withContext(Dispatchers.IO) {
                                ShizukuShellManager.executeCommand(command)
                            }
                            output += "\n$ ${command}\n${result.displayText()}\n"
                            commandHistory = commandHistory + CommandHistory(command, result.displayText(), result.success)
                            historyIndex = commandHistory.size
                            command = ""
                            isRunning = false
                        }
                    }
                },
                enabled = command.isNotBlank() && !isRunning && hasPermission,
                modifier = Modifier.weight(1f)
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
                    output = "DroidUtility Terminal v1.0\nType 'help' for available commands.\n\n"
                    commandHistory = emptyList()
                    historyIndex = -1
                }
            ) {
                Icon(Icons.Default.DeleteSweep, "Clear")
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(listOf(
                "pm list packages" to "List all packages",
                "pm list packages -d" to "Disabled",
                "pm list packages -e" to "Enabled",
                "dumpsys battery" to "Battery info",
                "getprop" to "System props",
                "df -h" to "Disk usage",
                "top -n 1" to "Processes"
            )) { (cmd, label) ->
                FilterChip(
                    selected = false,
                    onClick = { command = cmd },
                    label = { Text(label, fontSize = 11.sp) }
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEBLOAT TAB - Built-in Bloatware List
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun DebloatTabNew() {
    var selectedCategory by remember { mutableStateOf<BloatCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf<BloatApp?>(null) }
    var installedPackages by remember { mutableStateOf<Set<String>>(emptySet()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val hasPermission = remember { ShizukuShellManager.hasPermission() }

    LaunchedEffect(Unit) {
        if (hasPermission) {
            withContext(Dispatchers.IO) {
                installedPackages = ShizukuShellManager.executeCommand("pm list packages")
                    .output
                    .lines()
                    .filter { it.startsWith("package:") }
                    .map { it.removePrefix("package:").trim() }
                    .toSet()
            }
        }
        isLoading = false
    }

    val allBloat = BloatList.getAllBloatware()
    val manufacturer = android.os.Build.MANUFACTURER
    val manufacturerBloat = BloatList.getByManufacturer(manufacturer)

    val filteredApps = when {
        searchQuery.isNotBlank() -> BloatList.searchPackages(searchQuery)
        selectedCategory != null -> allBloat.filter { it.category == selectedCategory }
        manufacturerBloat.isNotEmpty() -> manufacturerBloat
        else -> allBloat
    }.distinctBy { it.packageName }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Text(
            text = "Debloat Manager",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "${filteredApps.size} apps • ${filteredApps.count { it.packageName in installedPackages }} installed",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search packages...") },
            leadingIcon = { Icon(Icons.Default.Search, "Search") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, "Clear")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null && searchQuery.isBlank(),
                    onClick = { selectedCategory = null },
                    label = { Text("All", fontSize = 11.sp) }
                )
            }
            item {
                FilterChip(
                    selected = selectedCategory == BloatCategory.OEM_BLOATWARE,
                    onClick = { selectedCategory = BloatCategory.OEM_BLOATWARE },
                    label = { Text("OEM", fontSize = 11.sp) }
                )
            }
            item {
                FilterChip(
                    selected = selectedCategory == BloatCategory.TRACKING_SPYWARE,
                    onClick = { selectedCategory = BloatCategory.TRACKING_SPYWARE },
                    label = { Text("Tracking", fontSize = 11.sp) }
                )
            }
            item {
                FilterChip(
                    selected = selectedCategory == BloatCategory.ADVERTISING,
                    onClick = { selectedCategory = BloatCategory.ADVERTISING },
                    label = { Text("Ads", fontSize = 11.sp) }
                )
            }
            item {
                FilterChip(
                    selected = selectedCategory == BloatCategory.CARRIER_APPS,
                    onClick = { selectedCategory = BloatCategory.CARRIER_APPS },
                    label = { Text("Carrier", fontSize = 11.sp) }
                )
            }
            item {
                FilterChip(
                    selected = selectedCategory == BloatCategory.SOCIAL_MEDIA,
                    onClick = { selectedCategory = BloatCategory.SOCIAL_MEDIA },
                    label = { Text("Social", fontSize = 11.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (!hasPermission) {
            Card(
                modifier = Modifier.fillMaxWidth(),
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
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(filteredApps.sortedByDescending { it.packageName in installedPackages }) { app ->
                    val isInstalled = app.packageName in installedPackages

                    BloatAppCard(
                        app = app,
                        isInstalled = isInstalled,
                        onAction = { showConfirmDialog = app }
                    )
                }
            }
        }
    }

    if (showConfirmDialog != null) {
        val app = showConfirmDialog!!
        AlertDialog(
            onDismissRequest = { showConfirmDialog = null },
            title = { Text("Disable ${app.name}?") },
            text = {
                Column {
                    Text("Package: ${app.packageName}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(app.description)
                    Spacer(modifier = Modifier.height(8.dp))
                    when (app.risk) {
                        BloatRisk.SAFE -> {
                            Text("Risk: Safe to disable", color = Color(0xFF4CAF50))
                        }
                        BloatRisk.CAUTION -> {
                            Text("Risk: May affect some features", color = Color(0xFFFF9800))
                        }
                        BloatRisk.ADVANCED -> {
                            Text("Risk: Advanced users only", color = Color(0xFFF44336))
                        }
                    }
                    if (app.alternatives.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Alternatives: ${app.alternatives}", fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Row {
                    TextButton(onClick = {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                ShizukuShellManager.executeCommand("pm disable-user --user 0 ${app.packageName}")
                            }
                            installedPackages = installedPackages - app.packageName
                            Toast.makeText(context, "${app.name} disabled", Toast.LENGTH_SHORT).show()
                        }
                        showConfirmDialog = null
                    }) {
                        Text("Disable")
                    }
                    TextButton(onClick = {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                ShizukuShellManager.executeCommand("pm uninstall --user 0 ${app.packageName}")
                            }
                            installedPackages = installedPackages - app.packageName
                            Toast.makeText(context, "${app.name} uninstalled", Toast.LENGTH_SHORT).show()
                        }
                        showConfirmDialog = null
                    }) {
                        Text("Uninstall")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun BloatAppCard(
    app: BloatApp,
    isInstalled: Boolean,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isInstalled) { onAction() },
        colors = CardDefaults.cardColors(
            containerColor = if (isInstalled)
                MaterialTheme.colorScheme.surfaceVariant
            else
                MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        app.name,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (!isInstalled) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "not installed",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Text(
                    app.packageName,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    app.description,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (isInstalled) {
                when (app.risk) {
                    BloatRisk.SAFE -> Icon(
                        Icons.Default.CheckCircle,
                        "Safe",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                    BloatRisk.CAUTION -> Icon(
                        Icons.Default.Warning,
                        "Caution",
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(20.dp)
                    )
                    BloatRisk.ADVANCED -> Icon(
                        Icons.Default.Error,
                        "Advanced",
                        tint = Color(0xFFF44336),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// STATUS TAB
// ═══════════════════════════════════════════════════════════════════════════════

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {
        Text(
            text = "System Status",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
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
        } else if (statusLines.isNotEmpty()) {
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
                            cleanValue.contains("good", ignoreCase = true) || cleanValue == "2" -> "Good"
                            cleanValue.contains("cold", ignoreCase = true) || cleanValue == "7" -> "Cold"
                            cleanValue.contains("overheat", ignoreCase = true) || cleanValue == "3" -> "Overheat"
                            cleanValue.contains("dead", ignoreCase = true) || cleanValue == "4" -> "Dead"
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    isLoading = true
                    errorMessage = null
                    scope.launch {
                        val results = ShizukuShellManager.executeCommands(commandList)
                        statusLines = results.map { result ->
                            if (result.success && result.output.isNotBlank()) {
                                result.output.trim()
                            } else {
                                "Unknown"
                            }
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Refresh, "Refresh")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Refresh")
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// SETTINGS TAB
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun SettingsTab(themeMode: MutableState<ThemeMode>) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp)
    ) {
        Text(
            text = "Settings",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Appearance",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Theme Mode",
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ThemeMode.values().forEach { mode ->
                        FilterChip(
                            selected = themeMode.value == mode,
                            onClick = {
                                themeMode.value = mode
                                ThemePreferences.setThemeMode(context, mode)
                            },
                            label = {
                                Text(
                                    when (mode) {
                                        ThemeMode.LIGHT -> "Light"
                                        ThemeMode.DARK -> "Dark"
                                        ThemeMode.AMOLED -> "AMOLED"
                                        ThemeMode.SYSTEM -> "System"
                                    },
                                    fontSize = 12.sp
                                )
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Shizuku Status",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                val available = ShizukuShellManager.checkAvailability()
                val permission = ShizukuShellManager.hasPermission()

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        if (available) Icons.Default.CheckCircle else Icons.Default.Error,
                        "Status",
                        tint = if (available) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (available) "Shizuku is running" else "Shizuku not running",
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        if (permission) Icons.Default.CheckCircle else Icons.Default.Error,
                        "Permission",
                        tint = if (permission) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (permission) "Permission granted" else "Permission not granted",
                        fontSize = 14.sp
                    )
                }

                if (!available || !permission) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (!available) {
                                Toast.makeText(context, "Please start Shizuku app", Toast.LENGTH_LONG).show()
                            } else {
                                if (context is Activity) {
                                    ShizukuShellManager.requestPermission(context)
                                }
                                Toast.makeText(context, "Check Shizuku app for permission request", Toast.LENGTH_LONG).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (!available) "Start Shizuku" else "Request Permission")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "About",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("DroidUtility v1.0.0", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "A powerful Android utility tool for debloating and terminal access.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "No root required - uses Shizuku for elevated privileges.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
