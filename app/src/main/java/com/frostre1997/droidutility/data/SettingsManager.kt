package com.frostre1997.droidutility.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsManager(private val context: Context) {

    // Theme
    fun getThemeModeFlow(): Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[SettingsDataStore.THEME_MODE] ?: SettingsDataStore.getDefaultThemeMode()
        }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { prefs ->
            prefs[SettingsDataStore.THEME_MODE] = mode
        }
    }

    fun getDynamicColorFlow(): Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[SettingsDataStore.USE_DYNAMIC_COLOR] ?: false
        }

    suspend fun setDynamicColor(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SettingsDataStore.USE_DYNAMIC_COLOR] = enabled
        }
    }

    // Terminal
    fun getTerminalFontSizeFlow(): Flow<Float> =
        context.dataStore.data.map { prefs ->
            prefs[SettingsDataStore.TERMINAL_FONT_SIZE] ?: SettingsDataStore.getDefaultTerminalFontSize()
        }

    suspend fun setTerminalFontSize(size: Float) {
        context.dataStore.edit { prefs ->
            prefs[SettingsDataStore.TERMINAL_FONT_SIZE] = size
        }
    }

    fun getTerminalFontFamilyFlow(): Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[SettingsDataStore.TERMINAL_FONT_FAMILY] ?: SettingsDataStore.getDefaultTerminalFontFamily()
        }

    suspend fun setTerminalFontFamily(family: String) {
        context.dataStore.edit { prefs ->
            prefs[SettingsDataStore.TERMINAL_FONT_FAMILY] = family
        }
    }

    // Custom fonts
    fun getCustomFontPathFlow(): Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[SettingsDataStore.CUSTOM_FONT_PATH] ?: SettingsDataStore.getDefaultCustomFontPath()
        }

    suspend fun setCustomFontPath(path: String) {
        context.dataStore.edit { prefs ->
            prefs[SettingsDataStore.CUSTOM_FONT_PATH] = path
        }
    }

    // Experimental
    suspend fun setExperimentalFeatureX(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SettingsDataStore.EXPERIMENTAL_FEATURE_X] = enabled
        }
    }

    fun getExperimentalFeatureXFlow(): Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[SettingsDataStore.EXPERIMENTAL_FEATURE_X] ?: SettingsDataStore.getDefaultBoolean()
        }

    suspend fun setExperimentalFeatureY(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SettingsDataStore.EXPERIMENTAL_FEATURE_Y] = enabled
        }
    }

    fun getExperimentalFeatureYFlow(): Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[SettingsDataStore.EXPERIMENTAL_FEATURE_Y] ?: SettingsDataStore.getDefaultBoolean()
        }

    // Debugging
    suspend fun setRecordLogs(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SettingsDataStore.RECORD_LOGS] = enabled
        }
    }

    fun getRecordLogsFlow(): Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[SettingsDataStore.RECORD_LOGS] ?: SettingsDataStore.getDefaultBoolean()
        }

    suspend fun setEnableTelemetry(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SettingsDataStore.ENABLE_TELEMETRY] = enabled
        }
    }

    fun getEnableTelemetryFlow(): Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[SettingsDataStore.ENABLE_TELEMETRY] ?: SettingsDataStore.getDefaultBoolean()
        }

    suspend fun setEnableCrashReports(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SettingsDataStore.ENABLE_CRASH_REPORTS] = enabled
        }
    }

    fun getEnableCrashReportsFlow(): Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[SettingsDataStore.ENABLE_CRASH_REPORTS] ?: SettingsDataStore.getDefaultBoolean()
        }
}
