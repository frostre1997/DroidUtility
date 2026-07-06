package com.frostre1997.droidutility

import android.content.Context
import android.content.SharedPreferences

enum class ThemeMode {
    LIGHT,
    DARK,
    AMOLED,
    SYSTEM
}

object ThemePreferences {
    private const val PREFS_NAME = "droidutility_prefs"
    private const val KEY_THEME_MODE = "theme_mode"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getThemeMode(context: Context): ThemeMode {
        val value = getPrefs(context).getString(KEY_THEME_MODE, "SYSTEM") ?: "SYSTEM"
        return try {
            ThemeMode.valueOf(value)
        } catch (e: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
    }

    fun setThemeMode(context: Context, mode: ThemeMode) {
        getPrefs(context).edit().putString(KEY_THEME_MODE, mode.name).apply()
    }
}
