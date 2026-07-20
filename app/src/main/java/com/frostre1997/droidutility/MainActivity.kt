package com.frostre1997.droidutility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.frostre1997.droidutility.data.SettingsManager
import com.frostre1997.droidutility.ui.theme.DroidUtilityTheme
import com.frostre1997.droidutility.ui.theme.ThemeMode
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsManager = SettingsManager(this)

        setContent {
            val themeModeFlow = settingsManager.getThemeModeFlow().collectAsState(initial = "AMOLED")
            val dynamicColorFlow = settingsManager.getDynamicColorFlow().collectAsState(initial = false)
            val themeMode by themeModeFlow
            val useDynamicColor by dynamicColorFlow

            // Convert string to ThemeMode enum
            val mode = try {
                ThemeMode.valueOf(themeMode)
            } catch (_: IllegalArgumentException) {
                ThemeMode.AMOLED
            }

            DroidUtilityTheme(
                themeMode = mode,
                useDynamicColor = useDynamicColor
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}
