package com.frostre1997.droidutility

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var themeMode by remember { mutableStateOf(ThemePreferences.getThemeMode(this)) }

            DroidUtilityTheme(themeMode = themeMode) {
                MainScreen(
                    themeMode = themeMode,
                    onThemeChanged = { newMode ->
                        themeMode = newMode
                        ThemePreferences.setThemeMode(this, newMode)
                    }
                )
            }
        }
    }
}

// ─── Theme ─────────────────────────────────────────────────────────────────

@Composable
fun DroidUtilityTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val systemDark = isSystemInDarkTheme()

    val isDark = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.AMOLED -> true
        ThemeMode.SYSTEM -> systemDark
    }

    val colorScheme = if (isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            dynamicDarkColorScheme(context)
        } else {
            darkColorScheme(
                primary = Color(0xFF90CAF9),
                secondary = Color(0xFF80CBC4),
                tertiary = Color(0xFFA5D6A7),
                background = if (themeMode == ThemeMode.AMOLED) Color.Black else Color(0xFF121212),
                surface = if (themeMode == ThemeMode.AMOLED) Color.Black else Color(0xFF1E1E1E),
                surfaceVariant = if (themeMode == ThemeMode.AMOLED) Color(0xFF1A1A1A) else Color(0xFF2C2C2C),
                onPrimary = Color(0xFF0D0D0D),
                onSecondary = Color(0xFF0D0D0D),
                onBackground = Color(0xFFE0E0E0),
                onSurface = Color(0xFFE0E0E0),
                error = Color(0xFFCF6679),
                errorContainer = Color(0xFFB00020),
                onError = Color.Black,
                onErrorContainer = Color.White,
            )
        }
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            dynamicLightColorScheme(context)
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

// ─── Reusable UI Components ──────────────────────────────────────────────

@Composable
fun GradientHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(bottom = 12.dp),
        style = MaterialTheme.typography.titleLarge.copy(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary
                )
            )
        )
    )
}

@Composable
fun SettingsCard(
    icon: ImageVector,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary)
            }
            content()
        }
    }
}

@Composable
fun rememberAppIcon(packageName: String, context: Context): ImageBitmap? {
    return remember(packageName) {
        try {
            val drawable = context.packageManager.getApplicationIcon(packageName)
            val bitmap = when (drawable) {
                is BitmapDrawable -> drawable.bitmap
                else -> {
                    val bmp = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bmp)
                    drawable.setBounds(0, 0, 48, 48)
                    drawable.draw(canvas)
                    bmp
                }
            }
            bitmap.asImageBitmap()
        } catch (e: Exception) { null }
    }
}

// ─── Main Screen ─────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    themeMode: ThemeMode,
    onThemeChanged: (ThemeMode) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        Triple("Home", Icons.Default.Home, "Home"),
        Triple("Terminal", Icons.Default.Terminal, "Execute commands"),
        Triple("Debloat", Icons.Default.DeleteSweep, "Remove bloatware"),
        Triple("Console", Icons.Default.Code, "Full terminal"),
        Triple("Settings", Icons.Default.Settings, "App settings")
    )

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp,
                shadowElevation = 4.dp
            ) {
                Column {
                    Text(
                        text = "DroidUtility",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp)
                    )
                    ScrollableTabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.Transparent,
                        edgePadding = 8.dp,
                        indicator = { tabPositions ->
                            val selectedPosition = tabPositions[selectedTab]
                            TabRowDefaults.Indicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(x = selectedPosition.left, y = 0.dp)
                                    .width(selectedPosition.width),
                                height = 3.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, (title, icon, _) ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = { Text(title, fontSize = 13.sp) },
                                icon = { Icon(icon, contentDescription = title, modifier = Modifier.size(20.dp)) },
                                selectedContentColor = MaterialTheme.colorScheme.primary,
                                unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn() + slideInHorizontally() togetherWith
                    fadeOut() + slideOutHorizontally()
                }
            ) { target ->
                when (target) {
                    0 -> HomeTab()
                    1 -> TerminalTab()
                    2 -> DebloatTab()
                    3 -> ConsoleTab()
                    4 -> SettingsTab(themeMode, onThemeChanged)
                }
            }
        }
    }
}

// ─── Terminal Tab ─────────────────────────────────────────────────────────

@Composable
fun TerminalTab() {
    var command by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("Waiting for command...") }
    var isRunning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val shizukuState by ShizukuShellManager.shizukuState.collectAsState()
    val hasPermission = shizukuState == ShizukuShellManager.ShizukuState.AVAILABLE_GRANTED
    val shizukuAvailable = shizukuState == ShizukuShellManager.ShizukuState.AVAILABLE_GRANTED ||
            shizukuState == ShizukuShellManager.ShizukuState.AVAILABLE_NO_PERMISSION

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        GradientHeader("Terminal")

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

                    if (shizukuAvailable && !hasPermission) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (context is Activity) {
                                    ShizukuShellManager.requestPermission(context)
                                    Toast.makeText(context, "Check Shizuku app", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Grant Permission")
                        }
                    }
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
            enabled = !isRunning && hasPermission,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
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

// ─── Debloat Tab ──────────────────────────────────────────────────────────

data class AppInfo(
    val packageName: String,
    val appName: String,
    val isSystem: Boolean,
    val isUser: Boolean
)

@Composable
fun DebloatTab() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var allPackages by remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    var filteredPackages by remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var isLoading by remember { mutableStateOf(true) }

    val shizukuState by ShizukuShellManager.shizukuState.collectAsState()
    val hasPermission = shizukuState == ShizukuShellManager.ShizukuState.AVAILABLE_GRANTED

    fun applyFilters() {
        val query = searchQuery.lowercase().trim()
        filteredPackages = allPackages.filter { app ->
            val matchesSearch = app.appName.lowercase().contains(query) ||
                    app.packageName.lowercase().contains(query)
            val matchesFilter = when (selectedFilter) {
                "All" -> true
                "System" -> app.isSystem
                "User" -> app.isUser
                else -> true
            }
            matchesSearch && matchesFilter
        }
    }

    fun loadPackages() {
        scope.launch {
            isLoading = true
            val packageManager = context.packageManager
            val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            val appInfos = installedApps.map { app ->
                val isSystem = (app.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                val isUpdatedSystem = (app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
                AppInfo(
                    packageName = app.packageName,
                    appName = packageManager.getApplicationLabel(app).toString(),
                    isSystem = isSystem || isUpdatedSystem,
                    isUser = !isSystem && !isUpdatedSystem
                )
            }.sortedBy { it.appName }

            allPackages = appInfos
            applyFilters()
            isLoading = false
        }
    }

    LaunchedEffect(Unit) { loadPackages() }
    LaunchedEffect(hasPermission) { if (hasPermission) loadPackages() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        GradientHeader("Debloat Manager")

        Text(
            text = "${allPackages.size} apps • ${allPackages.count { it.isUser }} user apps",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it; applyFilters() },
            placeholder = { Text("Search packages...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        val filters = listOf("All", "System", "User")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter; applyFilters() },
                    label = { Text(filter, fontSize = 12.sp) },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurface,
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                items(filteredPackages) { app ->
                    PackageItem(
                        app = app,
                        context = context,
                        onUninstall = {
                            if (!hasPermission) {
                                Toast.makeText(context, "Shizuku permission required", Toast.LENGTH_SHORT).show()
                                return@PackageItem
                            }
                            scope.launch {
                                val result = DebloatEngine.uninstallPackage(app.packageName)
                                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                                loadPackages()
                            }
                        },
                        onDisable = {
                            if (!hasPermission) {
                                Toast.makeText(context, "Shizuku permission required", Toast.LENGTH_SHORT).show()
                                return@PackageItem
                            }
                            scope.launch {
                                val result = DebloatEngine.disablePackage(app.packageName)
                                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                                loadPackages()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PackageItem(
    app: AppInfo,
    context: Context,
    onUninstall: () -> Unit,
    onDisable: () -> Unit
) {
    val icon = rememberAppIcon(app.packageName, context)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                if (icon != null) {
                    Image(
                        bitmap = icon,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().padding(6.dp)
                    )
                } else {
                    Icon(
                        Icons.Default.Android,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxSize().padding(6.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    app.appName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    app.packageName,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (app.isSystem) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "System",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = onUninstall,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Uninstall", modifier = Modifier.size(18.dp))
                }
                IconButton(
                    onClick = onDisable,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.Block, contentDescription = "Disable", modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

// ─── Settings Tab ─────────────────────────────────────────────────────────

@Composable
fun SettingsTab(
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var updateAvailable by remember { mutableStateOf<String?>(null) }
    var isChecking by remember { mutableStateOf(false) }

    val shizukuState by ShizukuShellManager.shizukuState.collectAsState()
    val shizukuAvailable = shizukuState == ShizukuShellManager.ShizukuState.AVAILABLE_GRANTED ||
            shizukuState == ShizukuShellManager.ShizukuState.AVAILABLE_NO_PERMISSION
    val hasPermission = shizukuState == ShizukuShellManager.ShizukuState.AVAILABLE_GRANTED

    LaunchedEffect(Unit) {
        isChecking = true
        updateAvailable = UpdateManager.checkForUpdate(context)
        isChecking = false
    }

    val currentVersion = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    } catch (e: Exception) { "1.0.0" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        GradientHeader("Settings")

        // Appearance
        SettingsCard(icon = Icons.Default.Palette, title = "Appearance") {
            Text("Theme Mode", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(4.dp))
            val themeOptions = listOf(ThemeMode.LIGHT, ThemeMode.DARK, ThemeMode.AMOLED, ThemeMode.SYSTEM)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                themeOptions.forEach { mode ->
                    FilterChip(
                        selected = currentTheme == mode,
                        onClick = { onThemeChange(mode) },
                        label = { Text(mode.name, fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface,
                        )
                    )
                }
            }
        }

        // Updates
        SettingsCard(icon = Icons.Default.SystemUpdate, title = "Updates") {
            if (isChecking) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else if (updateAvailable != null) {
                Text("✅ New version available!", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { UpdateManager.downloadAndInstall(context, updateAvailable!!) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Download & Install")
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("You're on the latest version (v$currentVersion)", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                }
            }
        }

        // Shizuku Status
        SettingsCard(icon = Icons.Default.Security, title = "Shizuku Status") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (shizukuAvailable) Icons.Default.CheckCircle else Icons.Default.Error,
                    contentDescription = null,
                    tint = if (shizukuAvailable) Color(0xFF4CAF50) else Color(0xFFEF5350)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (shizukuAvailable) "Shizuku is running" else "Shizuku is not running",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (hasPermission) Icons.Default.CheckCircle else Icons.Default.Error,
                    contentDescription = null,
                    tint = if (hasPermission) Color(0xFF4CAF50) else Color(0xFFEF5350)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (hasPermission) "Permission granted" else "Permission not granted",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            if (!shizukuAvailable || !hasPermission) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (context is Activity) {
                            ShizukuShellManager.requestPermission(context)
                            Toast.makeText(context, "Check Shizuku app", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = shizukuAvailable && !hasPermission,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Grant Shizuku Permission")
                }
            }
        }

        // About
        SettingsCard(icon = Icons.Default.Info, title = "About") {
            Text("DroidUtility v$currentVersion", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text("A powerful Android utility tool for debloating and terminal access.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            Text("No root required - uses Shizuku for elevated privileges.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
        }
    }
}
