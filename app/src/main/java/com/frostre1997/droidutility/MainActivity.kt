package com.frostre1997.droidutility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.frostre1997.droidutility.ui.theme.DroidUtilityTheme

class MainActivity : ComponentActivity() {
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsManager = SettingsManager(this)

        setContent {
            val themeModeFlow = settingsManager.getThemeModeFlow().collectAsState(initial = "AMOLED")
            val dynamicColorFlow = settingsManager.getDynamicColorFlow().collectAsState(initial = false)
            val themeMode = ThemeMode.valueOf(themeModeFlow.value)
            val useDynamicColor = dynamicColorFlow.value

            DroidUtilityTheme(
                themeMode = themeMode,
                useDynamicColor = useDynamicColor
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}
