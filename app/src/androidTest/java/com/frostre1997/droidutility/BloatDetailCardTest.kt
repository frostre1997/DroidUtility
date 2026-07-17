package com.frostre1997.droidutility

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.frostre1997.droidutility.ui.components.BloatDetailCard
import com.frostre1997.droidutility.ui.theme.DroidUtilityTheme
import org.junit.Rule
import org.junit.Test

class BloatDetailCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun nullAppShowsSelectAnApp() {
        composeTestRule.setContent {
            DroidUtilityTheme {
                BloatDetailCard(app = null, onAction = {})
            }
        }
        composeTestRule.onNodeWithText("Select an app").assertExists()
    }

    @Test
    fun nonNullAppShowsDetails() {
        val app = BloatApp(
            packageName = "com.test.app",
            name = "Test App",
            description = "A test application",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE,
            alternatives = "Alt1"
        )
        composeTestRule.setContent {
            DroidUtilityTheme {
                BloatDetailCard(app = app, onAction = {})
            }
        }
        composeTestRule.onNodeWithText("Test App").assertExists()
        composeTestRule.onNodeWithText("com.test.app").assertExists()
        composeTestRule.onNodeWithText("A test application").assertExists()
        composeTestRule.onNodeWithText("Alternatives: Alt1").assertExists()
    }

    @Test
    fun actionButtonTriggersCallback() {
        var actionClicked = false
        val app = BloatApp(
            packageName = "com.test",
            name = "Test",
            description = "Desc",
            category = BloatCategory.GAMES
        )
        composeTestRule.setContent {
            DroidUtilityTheme {
                BloatDetailCard(app = app, onAction = { actionClicked = true })
            }
        }
        composeTestRule.onNodeWithText("Disable / Uninstall").performClick()
        assert(actionClicked) { "Action button should trigger callback" }
    }

    @Test
    fun blankAlternativesNotShown() {
        val app = BloatApp(
            packageName = "com.test",
            name = "Test",
            description = "Desc",
            category = BloatCategory.OEM_BLOATWARE,
            alternatives = ""
        )
        composeTestRule.setContent {
            DroidUtilityTheme {
                BloatDetailCard(app = app, onAction = {})
            }
        }
        composeTestRule.onNodeWithText("Alternatives:").assertDoesNotExist()
    }
}
