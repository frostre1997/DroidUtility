package com.frostre1997.droidutility.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frostre1997.droidutility.data.SettingsManager
import com.frostre1997.droidutility.ui.theme.ThemeMode

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }

    // Collect settings states
    val themeMode by settingsManager.getThemeModeFlow().collectAsState(initial = "AMOLED")
    val dynamicColor by settingsManager.getDynamicColorFlow().collectAsState(initial = false)
    val terminalFontSize by settingsManager.getTerminalFontSizeFlow().collectAsState(initial = 16f)
    val terminalFontFamily by settingsManager.getTerminalFontFamilyFlow().collectAsState(initial = "monospace")
    val customFontPath by settingsManager.getCustomFontPathFlow().collectAsState(initial = "")
    val experimentalFeatureX by settingsManager.getExperimentalFeatureXFlow().collectAsState(initial = false)
    val experimentalFeatureY by settingsManager.getExperimentalFeatureYFlow().collectAsState(initial = false)
    val recordLogs by settingsManager.getRecordLogsFlow().collectAsState(initial = false)
    val enableTelemetry by settingsManager.getEnableTelemetryFlow().collectAsState(initial = false)
    val enableCrashReports by settingsManager.getEnableCrashReportsFlow().collectAsState(initial = false)

    // File picker for custom font
    val fontPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Save the URI as string
            settingsManager.setCustomFontPath(it.toString())
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Appearance
        item {
            SettingsSection(title = "Appearance") {
                // Theme radio group
                Text("Theme", color = Color.White, fontSize = 14.sp)
                ThemeMode.values().forEach { mode ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = themeMode == mode.name,
                            onClick = {
                                settingsManager.setThemeMode(mode.name)
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF4FC3F7)
                            )
                        )
                        Text(mode.name, color = Color.White, modifier = Modifier.padding(start = 8.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Dynamic Color toggle (Android 12+)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dynamic Color", color = Color.White, modifier = Modifier.weight(1f))
                    Switch(
                        checked = dynamicColor,
                        onCheckedChange = { settingsManager.setDynamicColor(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF4FC3F7),
                            checkedTrackColor = Color(0xFF4FC3F7).copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }

        // Terminal
        item {
            SettingsSection(title = "Terminal") {
                Text("Font Size", color = Color.White)
                Slider(
                    value = terminalFontSize,
                    onValueChange = { settingsManager.setTerminalFontSize(it) },
                    valueRange = 10f..30f,
                    steps = 20,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF4FC3F7),
                        activeTrackColor = Color(0xFF4FC3F7)
                    )
                )
                Text("Current: ${terminalFontSize.toInt()}sp", color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                // Font family dropdown – using a simple list of available families
                Text("Font Family", color = Color.White)
                val fontFamilies = listOf("monospace", "sans-serif", "serif", "sans-serif-condensed")
                DropdownMenuExample(
                    current = terminalFontFamily,
                    options = fontFamilies,
                    onSelect = { settingsManager.setTerminalFontFamily(it) }
                )
            }
        }

        // Font Management (custom fonts)
        item {
            SettingsSection(title = "Font Management") {
                Text("Custom Font", color = Color.White)
                Button(
                    onClick = { fontPickerLauncher.launch("application/octet-stream") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4FC3F7))
                ) {
                    Text("Upload Custom Font (.ttf)", color = Color.Black)
                }
                if (customFontPath.isNotEmpty()) {
                    Text("Current: $customFontPath", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }

        // Experimental
        item {
            SettingsSection(title = "Experimental") {
                SettingSwitch(
                    label = "Experimental Feature X",
                    checked = experimentalFeatureX,
                    onCheckedChange = { settingsManager.setExperimentalFeatureX(it) }
                )
                SettingSwitch(
                    label = "Experimental Feature Y",
                    checked = experimentalFeatureY,
                    onCheckedChange = { settingsManager.setExperimentalFeatureY(it) }
                )
            }
        }

        // Debugging
        item {
            SettingsSection(title = "Debugging") {
                SettingSwitch(
                    label = "Record Debug Logs",
                    checked = recordLogs,
                    onCheckedChange = { settingsManager.setRecordLogs(it) }
                )
                SettingSwitch(
                    label = "Enable Telemetry",
                    checked = enableTelemetry,
                    onCheckedChange = { settingsManager.setEnableTelemetry(it) }
                )
                SettingSwitch(
                    label = "Enable Crash Reports",
                    checked = enableCrashReports,
                    onCheckedChange = { settingsManager.setEnableCrashReports(it) }
                )
            }
        }

        // About
        item {
            SettingsSection(title = "About") {
                Text("Version 1.0.5-beta.6", color = Color.White)
                Text("Built with 🤍 using Jetpack Compose", color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { /* open licenses */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text("View Open Source Licenses", color = Color(0xFF4FC3F7))
                }
                Button(
                    onClick = { /* open developer info */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text("Developer Info", color = Color(0xFF4FC3F7))
                }
            }
        }
    }
}

// Helper composable for a settings section
@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

// Helper for a switch row
@Composable
fun SettingSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(label, color = Color.White, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF4FC3F7),
                checkedTrackColor = Color(0xFF4FC3F7).copy(alpha = 0.5f)
            )
        )
    }
}

// Helper dropdown (simple implementation)
@Composable
fun DropdownMenuExample(
    current: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White,
                containerColor = Color(0xFF2A2A2A)
            )
        ) {
            Text(current)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF1A1A1A))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = Color.White) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    },
                    modifier = Modifier.background(Color.Transparent)
                )
            }
        }
    }
}
