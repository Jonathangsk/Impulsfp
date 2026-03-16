package com.impulsfp.mobile.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.impulsfp.mobile.MainActivity
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun login_screen_displays_basic_elements() {
        composeRule.onNodeWithText("Usuari").assertExists()
        composeRule.onNodeWithText("Contrasenya").assertExists()
        composeRule.onNodeWithText("Iniciar Sessió").assertIsDisplayed()
    }

    @Test
    fun login_with_empty_fields_show_error_message() {
            composeRule.onNodeWithTag("loginButton").performClick()
        composeRule.onNodeWithText(
            "Cal introduir l'usuari i la contrasenya per iniciar sessió."
        ).assertIsDisplayed()
    }

    @Ignore("Dependrà del missatge que retorni el backend real")
    @Test
    fun login_with_wrong_credentials_shows_error_message() {
        composeRule.onNodeWithTag("usernameField").performTextInput("test")
        composeRule.onNodeWithTag("passwordField").performTextInput("1234")

        composeRule.onNodeWithTag("loginButton").performClick()

        composeRule.waitUntil(3000) {
            composeRule
                .onAllNodesWithText("Credencials incorrectes")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("Credencials incorrectes").assertIsDisplayed()
    }


}