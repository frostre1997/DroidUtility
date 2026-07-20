package com.frostre1997.droidutility.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore

// Make this extension public (no private modifier)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object SettingsDataStore {
    // Theme keys
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")

    // Terminal keys
    val TERMINAL_FONT_SIZE = floatPreferencesKey("terminal_font_size")
    val TERMINAL_FONT_FAMILY = stringPreferencesKey("terminal_font_family")

    // Font keys (custom fonts)
    val CUSTOM_FONT_PATH = stringPreferencesKey("custom_font_path")

    // Experimental toggles
    val EXPERIMENTAL_FEATURE_X = booleanPreferencesKey("experimental_feature_x")
    val EXPERIMENTAL_FEATURE_Y = booleanPreferencesKey("experimental_feature_y")

    // Debugging toggles
    val RECORD_LOGS = booleanPreferencesKey("record_logs")
    val ENABLE_TELEMETRY = booleanPreferencesKey("enable_telemetry")
    val ENABLE_CRASH_REPORTS = booleanPreferencesKey("enable_crash_reports")

    // Default values
    fun getDefaultThemeMode(): String = "AMOLED"
    fun getDefaultTerminalFontSize(): Float = 16f
    fun getDefaultTerminalFontFamily(): String = "monospace"
    fun getDefaultCustomFontPath(): String = ""
    fun getDefaultBoolean(): Boolean = false
}
