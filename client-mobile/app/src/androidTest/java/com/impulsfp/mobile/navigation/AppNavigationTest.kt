/**package com.impulsfp.mobile.navigation

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import org.junit.Rule
import org.junit.Test

/**
 * Tests instrumentats de navegació de l'aplicació.
 *
 * Verifiquen:
 * - que la navegació va del login al menú
 * - que el logout retorna a la pantalla de login
 *
 *  @author abenitez
 */
class AppNavigationTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun valid_login_navigates_to_menu() {
        composeRule.setContent {
            AppNavigation(
                loginScreen = { onLoginSuccess ->
                    Button(
                        onClick = onLoginSuccess,
                        modifier = Modifier.testTag("fakeLoginButton")
                    ) {
                        Text("Fake Login")
                    }
                },
                menuScreen = {
                    Text("Menú alumne")
                }
            )
        }

        composeRule.onNodeWithTag("fakeLoginButton").performClick()
        composeRule.onNodeWithText("Menú alumne").assertExists()
    }

    @Test
    fun logout_returns_to_login_screen() {
        composeRule.setContent {
            AppNavigation(
                loginScreen = { onLoginSuccess ->
                    Button(
                        onClick = onLoginSuccess,
                        modifier = Modifier.testTag("fakeLoginButton")
                    ) {
                        Text("Fake Login")
                    }
                },
                menuScreen = { onLogout ->
                    Button(
                        onClick = onLogout,
                        modifier = Modifier.testTag("logoutButton")
                    ) {
                        Text("Tancar sessió")
                    }
                }
            )
        }

        composeRule.onNodeWithTag("fakeLoginButton").performClick()
        composeRule.onNodeWithTag("logoutButton").performClick()
        composeRule.onNodeWithTag("fakeLoginButton").assertExists()
    }
}*/