package com.frostre1997.droidutility.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frostre1997.droidutility.data.SettingsManager
import com.frostre1997.droidutility.ui.theme.ThemeMode
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    val coroutineScope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }

    // Collect all states
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
    val language by settingsManager.getLanguageFlow().collectAsState(initial = "en")
    val colorfulWorkflowCards by settingsManager.getColorfulWorkflowCardsFlow().collectAsState(initial = false)
    val liquidGlassNav by settingsManager.getLiquidGlassNavFlow().collectAsState(initial = false)
    val uiScale by settingsManager.getUIScaleFlow().collectAsState(initial = 1.0f)

    // File picker for custom font
    val fontPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            coroutineScope.launch {
                settingsManager.setCustomFontPath(it.toString())
            }
        }
    }

    // Build flat list with groups
    val allItems = buildList {
        // Language
        add(SettingsItem.Group("Language"))
        add(SettingsItem.Dropdown("Language", languageToDisplay(language), listOf("English", "Spanish", "French", "German", "Italian")) { lang ->
            coroutineScope.launch {
                settingsManager.setLanguage(displayToCode(lang))
            }
        })
        add(SettingsItem.Label("Choose app display language"))

        // Appearance
        add(SettingsItem.Group("Appearance"))
        add(SettingsItem.ThemeRadioGroup(themeMode, onThemeSelected = { selected ->
            coroutineScope.launch { settingsManager.setThemeMode(selected) }
        }))
        add(SettingsItem.Switch("Dynamic Color", dynamicColor) {
            coroutineScope.launch { settingsManager.setDynamicColor(it) }
        })
        add(SettingsItem.Switch("Colorful workflow cards (Beta)", colorfulWorkflowCards) {
            coroutineScope.launch { settingsManager.setColorfulWorkflowCards(it) }
        })
        add(SettingsItem.Switch("Liquid Glass navigation bar", liquidGlassNav) {
            coroutineScope.launch { settingsManager.setLiquidGlassNav(it) }
        })
        add(SettingsItem.Slider("Scale", uiScale, 0.5f..1.5f) { newScale ->
            coroutineScope.launch { settingsManager.setUIScale(newScale) }
        })

        // Terminal
        add(SettingsItem.Group("Terminal"))
        add(SettingsItem.Slider("Font Size", terminalFontSize, 10f..30f) { newSize ->
            coroutineScope.launch { settingsManager.setTerminalFontSize(newSize) }
        })
        add(SettingsItem.Dropdown("Font Family", terminalFontFamily, listOf("monospace", "sans-serif", "serif", "sans-serif-condensed")) { family ->
            coroutineScope.launch { settingsManager.setTerminalFontFamily(family) }
        })

        // Font Management
        add(SettingsItem.Group("Font Management"))
        add(SettingsItem.Button("Upload Custom Font (.ttf)") {
            fontPickerLauncher.launch("application/octet-stream")
        })
        if (customFontPath.isNotEmpty()) {
            add(SettingsItem.Label("Current: $customFontPath"))
        }

        // Experimental
        add(SettingsItem.Group("Experimental"))
        add(SettingsItem.SwitchWithDesc(
            label = "Shizuku ADB Fallback",
            description = "Fall back to ADB over Wi‑Fi if Shizuku is unavailable (debug builds only)",
            checked = experimentalFeatureX,
            onCheckedChange = { coroutineScope.launch { settingsManager.setExperimentalFeatureX(it) } }
        ))
        add(SettingsItem.SwitchWithDesc(
            label = "Force Dynamic Color",
            description = "Apply Material You theming even on Android 10 and below (experimental)",
            checked = experimentalFeatureY,
            onCheckedChange = { coroutineScope.launch { settingsManager.setExperimentalFeatureY(it) } }
        ))

        // Debugging
        add(SettingsItem.Group("Debugging"))
        add(SettingsItem.Switch("Record Debug Logs", recordLogs) {
            coroutineScope.launch { settingsManager.setRecordLogs(it) }
        })
        add(SettingsItem.Switch("Enable Telemetry", enableTelemetry) {
            coroutineScope.launch { settingsManager.setEnableTelemetry(it) }
        })
        add(SettingsItem.Switch("Enable Crash Reports", enableCrashReports) {
            coroutineScope.launch { settingsManager.setEnableCrashReports(it) }
        })

        // General Settings
        add(SettingsItem.Group("General Settings"))
        add(SettingsItem.Action("Module Config") { /* navigate */ })
        add(SettingsItem.Action("Global Variables") { /* navigate */ })
        add(SettingsItem.Action("Model Config") { /* navigate */ })

        // About
        add(SettingsItem.Group("About"))
        add(SettingsItem.Label("Version 1.0.5-beta.6"))
        add(SettingsItem.Label("Built with 🤍 using Jetpack Compose"))
        add(SettingsItem.Action("View Open Source Licenses") { /* open licenses */ })
        add(SettingsItem.Action("Developer Info") { /* open developer info */ })
    }

    // Filter items based on search
    val filteredItems = if (searchQuery.isBlank()) {
        allItems
    } else {
        allItems.filter { item ->
            when (item) {
                is SettingsItem.Group -> false
                is SettingsItem.Switch -> item.label.contains(searchQuery, ignoreCase = true)
                is SettingsItem.SwitchWithDesc -> item.label.contains(searchQuery, ignoreCase = true) || item.description.contains(searchQuery, ignoreCase = true)
                is SettingsItem.Slider -> item.label.contains(searchQuery, ignoreCase = true)
                is SettingsItem.Dropdown -> item.label.contains(searchQuery, ignoreCase = true) || item.current.contains(searchQuery, ignoreCase = true)
                is SettingsItem.Button -> item.label.contains(searchQuery, ignoreCase = true)
                is SettingsItem.Label -> item.text.contains(searchQuery, ignoreCase = true)
                is SettingsItem.Action -> item.label.contains(searchQuery, ignoreCase = true)
                is SettingsItem.ThemeRadioGroup -> true
                else -> false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search settings", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            textStyle = TextStyle(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0xFF1A1A1A), shape = RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Group items by section – each group is a card
            val grouped = filteredItems.fold(mutableListOf<MutableList<SettingsItem>>()) { acc, item ->
                if (item is SettingsItem.Group) {
                    acc.add(mutableListOf(item))
                } else if (acc.isNotEmpty()) {
                    acc.last().add(item)
                } else {
                    acc.add(mutableListOf(item))
                }
                acc
            }

            grouped.forEach { groupItems ->
                val groupTitle = groupItems.firstOrNull() as? SettingsItem.Group
                val contentItems = groupItems.drop(1)

                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            // Group title as header inside card
                            groupTitle?.let {
                                Text(
                                    text = it.title,
                                    color = Color(0xFF4FC3F7),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                )
                            }

                            contentItems.forEach { item ->
                                when (item) {
                                    is SettingsItem.Switch -> {
                                        SettingSwitchRow(
                                            label = item.label,
                                            checked = item.checked,
                                            onCheckedChange = item.onCheckedChange
                                        )
                                    }
                                    is SettingsItem.SwitchWithDesc -> {
                                        SettingSwitchRowWithDesc(
                                            label = item.label,
                                            description = item.description,
                                            checked = item.checked,
                                            onCheckedChange = item.onCheckedChange
                                        )
                                    }
                                    is SettingsItem.Slider -> {
                                        SettingSlider(
                                            label = item.label,
                                            value = item.value,
                                            range = item.range,
                                            onValueChange = item.onValueChange
                                        )
                                    }
                                    is SettingsItem.Dropdown -> {
                                        SettingDropdown(
                                            label = item.label,
                                            current = item.current,
                                            options = item.options,
                                            onSelect = item.onSelect
                                        )
                                    }
                                    is SettingsItem.Button -> {
                                        SettingActionButton(
                                            label = item.label,
                                            onClick = item.onClick
                                        )
                                    }
                                    is SettingsItem.Label -> {
                                        SettingLabel(text = item.text)
                                    }
                                    is SettingsItem.Action -> {
                                        SettingAction(
                                            label = item.label,
                                            onClick = item.onClick
                                        )
                                    }
                                    is SettingsItem.ThemeRadioGroup -> {
                                        ThemeRadioGroup(
                                            themeMode = item.currentTheme,
                                            onThemeSelected = item.onThemeSelected
                                        )
                                    }
                                    else -> {}
                                }
                                // Add divider between items except after the last one
                                if (contentItems.indexOf(item) < contentItems.size - 1) {
                                    Divider(
                                        color = Color.Gray.copy(alpha = 0.2f),
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------- Helper functions for language ----------
fun languageToDisplay(code: String): String = when (code) {
    "en" -> "English"
    "es" -> "Spanish"
    "fr" -> "French"
    "de" -> "German"
    "it" -> "Italian"
    else -> "English"
}

fun displayToCode(display: String): String = when (display) {
    "English" -> "en"
    "Spanish" -> "es"
    "French" -> "fr"
    "German" -> "de"
    "Italian" -> "it"
    else -> "en"
}

// ---------- Data classes ----------
sealed class SettingsItem {
    data class Group(val title: String) : SettingsItem()
    data class Switch(val label: String, val checked: Boolean, val onCheckedChange: (Boolean) -> Unit) : SettingsItem()
    data class SwitchWithDesc(val label: String, val description: String, val checked: Boolean, val onCheckedChange: (Boolean) -> Unit) : SettingsItem()
    data class Slider(val label: String, val value: Float, val range: ClosedFloatingPointRange<Float>, val onValueChange: (Float) -> Unit) : SettingsItem()
    data class Dropdown(val label: String, val current: String, val options: List<String>, val onSelect: (String) -> Unit) : SettingsItem()
    data class Button(val label: String, val onClick: () -> Unit) : SettingsItem()
    data class Label(val text: String) : SettingsItem()
    data class Action(val label: String, val onClick: () -> Unit) : SettingsItem()
    data class ThemeRadioGroup(val currentTheme: String, val onThemeSelected: (String) -> Unit) : SettingsItem()
}

// ---------- Composables with larger touch targets ----------

@Composable
fun SettingSwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onCheckedChange(!checked) }
    ) {
        Text(
            text = label,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
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

@Composable
fun SettingSwitchRowWithDesc(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onCheckedChange(!checked) }
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = Color.White)
            Text(description, color = Color.Gray, fontSize = 12.sp)
        }
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

@Composable
fun SettingSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(label, color = Color.White)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            steps = (range.endInclusive - range.start).toInt() * 2,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF4FC3F7),
                activeTrackColor = Color(0xFF4FC3F7)
            )
        )
        Text("Current: ${(value * 100).toInt()}%", color = Color.Gray)
    }
}

@Composable
fun SettingDropdown(
    label: String,
    current: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { expanded = true }
    ) {
        Text(label, color = Color.White, modifier = Modifier.weight(1f))
        Text(current, color = Color(0xFF4FC3F7))
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF1A1A1A))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color 
