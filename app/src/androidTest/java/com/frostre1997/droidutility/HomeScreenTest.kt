package com.frostre1997.droidutility

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.frostre1997.droidutility.ui.screens.HomeScreen
import com.frostre1997.droidutility.ui.theme.DroidUtilityTheme
import org.junit.Rule
import org.junit.Test

class DroidUtilityAppTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreenRendersTitle() {
        composeTestRule.setContent {
            DroidUtilityTheme {
                HomeScreen(
                    onOpenTerminal = {},
                    onOpenDebloat = {},
                    onOpenConsole = {},
                    onOpenSettings = {}
                )
            }
        }
        composeTestRule.onNodeWithText("DroidUtility").assertExists()
    }

    @Test
    fun homeScreenRendersSubtitle() {
        composeTestRule.setContent {
            DroidUtilityTheme {
                HomeScreen(
                    onOpenTerminal = {},
                    onOpenDebloat = {},
                    onOpenConsole = {},
                    onOpenSettings = {}
                )
            }
        }
        composeTestRule.onNodeWithText("A powerful non-root utility suite for Android.").assertExists()
    }

    @Test
    fun homeScreenTerminalCardClickable() {
        var clicked = false
        composeTestRule.setContent {
            DroidUtilityTheme {
                HomeScreen(
                    onOpenTerminal = { clicked = true },
                    onOpenDebloat = {},
                    onOpenConsole = {},
                    onOpenSettings = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Terminal").performClick()
        assert(clicked) { "Terminal click should trigger callback" }
    }

    @Test
    fun homeScreenDebloatCardClickable() {
        var clicked = false
        composeTestRule.setContent {
            DroidUtilityTheme {
                HomeScreen(
                    onOpenTerminal = {},
                    onOpenDebloat = { clicked = true },
                    onOpenConsole = {},
                    onOpenSettings = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Debloat").performClick()
        assert(clicked) { "Debloat click should trigger callback" }
    }

    @Test
    fun homeScreenConsoleCardClickable() {
        var clicked = false
        composeTestRule.setContent {
            DroidUtilityTheme {
                HomeScreen(
                    onOpenTerminal = {},
                    onOpenDebloat = {},
                    onOpenConsole = { clicked = true },
                    onOpenSettings = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Console").performClick()
        assert(clicked) { "Console click should trigger callback" }
    }

    @Test
    fun homeScreenSettingsCardClickable() {
        var clicked = false
        composeTestRule.setContent {
            DroidUtilityTheme {
                HomeScreen(
                    onOpenTerminal = {},
                    onOpenDebloat = {},
                    onOpenConsole = {},
                    onOpenSettings = { clicked = true }
                )
            }
        }
        composeTestRule.onNodeWithText("Settings").performClick()
        assert(clicked) { "Settings click should trigger callback" }
    }
}
