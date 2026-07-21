package com.frostre1997.droidutility.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AccentBlue = Color(0xFF4FC3F7)
private val AccentGreen = Color(0xFF66BB6A)
private val AccentRed = Color(0xFFEF5350)

// Light theme – white background, dark text
private val LightColorScheme = lightColorScheme(
    primary = AccentBlue,
    secondary = AccentGreen,
    tertiary = AccentRed,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    primaryContainer = AccentBlue.copy(alpha = 0.2f),
    onPrimaryContainer = Color.Black
)

// Standard dark – dark grey background, light text
private val DarkColorScheme = darkColorScheme(
    primary = AccentBlue,
    secondary = AccentGreen,
    tertiary = AccentRed,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    primaryContainer = AccentBlue.copy(alpha = 0.2f),
    onPrimaryContainer = Color.White
)

// AMOLED – pure black background, light text
private val AmoledColorScheme = darkColorScheme(
    primary = AccentBlue,
    secondary = AccentGreen,
    tertiary = AccentRed,
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    primaryContainer = AccentBlue.copy(alpha = 0.2f),
    onPrimaryContainer = Color.White
)

enum class ThemeMode {
    LIGHT, DARK, AMOLED, SYSTEM
}

@Composable
fun DroidUtilityTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    useDynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()

    val colorScheme = when {
        useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (themeMode == ThemeMode.LIGHT || (themeMode == ThemeMode.SYSTEM && !isDark)) {
                dynamicLightColorScheme(context)
            } else {
                dynamicDarkColorScheme(context)
            }
        }
        themeMode == ThemeMode.LIGHT -> LightColorScheme
        themeMode == ThemeMode.DARK -> DarkColorScheme
        themeMode == ThemeMode.AMOLED -> AmoledColorScheme
        else -> { // SYSTEM
            if (isDark) DarkColorScheme else LightColorScheme
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()

            val controller = WindowCompat.getInsetsController(window, view)
            // Determine if icons should be light or dark based on theme
            val isLightTheme = when {
                themeMode == ThemeMode.LIGHT -> true
                themeMode == ThemeMode.SYSTEM && !isDark -> true
                else -> false
            }
            // If dynamic color is used, we ignore the above and just set based on the actual color scheme
            if (useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Dynamic color might produce a light or dark scheme; we'll assume it's dark
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            } else {
                controller.isAppearanceLightStatusBars = isLightTheme
                controller.isAppearanceLightNavigationBars = isLightTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}
