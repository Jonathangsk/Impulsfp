package com.impulsfp.mobile.navigation

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.impulsfp.mobile.MainActivity
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import kotlin.collections.isNotEmpty

class AppNavigationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Ignore("Depèn de credencials fake. S'adaptarà quan hi hagi backend real.")
    @Test
    fun valid_login_navigates_to_menu() {
        composeRule.onNodeWithTag("usernameField").performTextInput("alumne")
        composeRule.onNodeWithTag("passwordField").performTextInput("1234")
        composeRule.onNodeWithTag("loginButton").performClick()

        composeRule.waitUntil(3000) {
            composeRule
                .onAllNodesWithText("Menú alumne")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("Menú alumne").assertExists()
    }

    @Test
    fun logout_returns_to_login_screen() {
        composeRule.onNodeWithTag("usernameField").performTextInput("alumne")
        composeRule.onNodeWithTag("passwordField").performTextInput("1234")
        composeRule.onNodeWithTag("loginButton").performClick()

        composeRule.waitUntil(3000) {
            composeRule
                .onAllNodesWithTag("logoutButton")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("logoutButton").performClick()
        composeRule.onNodeWithText("Iniciar Sessió").assertExists()
    }
}