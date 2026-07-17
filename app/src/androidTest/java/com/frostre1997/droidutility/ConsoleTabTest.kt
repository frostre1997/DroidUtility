package com.frostre1997.droidutility

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.frostre1997.droidutility.tabs.ConsoleTab
import com.frostre1997.droidutility.ui.theme.DroidUtilityTheme
import org.junit.Rule
import org.junit.Test

class ConsoleTabTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun consoleTabRendersTitle() {
        composeTestRule.setContent {
            DroidUtilityTheme {
                ConsoleTab(onBack = {})
            }
        }
        composeTestRule.onNodeWithText("Console").assertExists()
    }

    @Test
    fun consoleTabRendersDefaultCommand() {
        composeTestRule.setContent {
            DroidUtilityTheme {
                ConsoleTab(onBack = {})
            }
        }
        composeTestRule.onNodeWithText("pm list packages").assertExists()
    }

    @Test
    fun consoleTabRunButtonAppendsOutput() {
        composeTestRule.setContent {
            DroidUtilityTheme {
                ConsoleTab(onBack = {})
            }
        }
        composeTestRule.onNodeWithText("Run").performClick()
        composeTestRule.onNodeWithText("Executed: pm list packages").assertExists()
    }

    @Test
    fun consoleTabBackButtonTriggersCallback() {
        var backClicked = false
        composeTestRule.setContent {
            DroidUtilityTheme {
                ConsoleTab(onBack = { backClicked = true })
            }
        }
        composeTestRule.onNodeWithText("Back").performClick()
        assert(backClicked) { "Back button should trigger callback" }
    }
}
