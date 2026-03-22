package com.impulsfp.mobile.ui

import org.junit.Assert.assertTrue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.impulsfp.mobile.communications.AuthController
import com.impulsfp.mobile.data.User
import org.junit.Rule
import org.junit.Test

/**
 * Test instrumentats de la pantalla de login.
 *
 * Verifiquen:
 * -la presència dels elements bàsics de la pantalla
 * - la validació quan els camps són buits
 * - la visualització quan els camps són buits
 * - l'execució del callback quan el login és correcte
 *
 *  @author abenitez
 */
class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private class FakeAuthControllerSuccess : AuthController() {
        override suspend fun login(username: String, password: String): Result<User> {
            return Result.success(
                User(
                    username = username,
                    role = "ADMIN",
                    sessionId = "session-123"
                )
            )
        }
    }

    private class FakeAuthControllerFailure : AuthController() {
        override suspend fun login(username: String, password: String): Result<User> {
            return Result.failure(Exception("Usuari o contrasenya incorrectes"))
        }
    }

    @Test
    fun login_screen_displays_basic_elements() {
        composeRule.setContent {
            @Suppress("ViewModelConstructorInComposable")
            LoginScreen(
                onLoginSuccess = {},
                loginViewModel = LoginViewModel()
            )
        }
        composeRule.onNodeWithText("Usuari").assertExists()
        composeRule.onNodeWithText("Contrasenya").assertExists()
        composeRule.onNodeWithText("Iniciar Sessió").assertExists()

    }

    @Test
    fun login_with_empty_fields_show_error_message() {

        composeRule.setContent {
            @Suppress("ViewModelConstructorInComposable")
            LoginScreen(
                onLoginSuccess = {},
                loginViewModel = LoginViewModel()
            )
        }

        composeRule.onNodeWithTag("loginButton").performClick()
        composeRule.onNodeWithText(
            "Cal introduir l'usuari i la contrasenya per iniciar sessió."
        ).assertIsDisplayed()
    }

    @Test
    fun login_with_wrong_credentials_shows_error_message() {

        composeRule.setContent {
            @Suppress("ViewModelConstructorInComposable")
            LoginScreen(
                onLoginSuccess = {},
                loginViewModel = LoginViewModel(FakeAuthControllerFailure())
            )
        }
        composeRule.onNodeWithTag("usernameField").performTextInput("test")
        composeRule.onNodeWithTag("passwordField").performTextInput("1234")
        composeRule.onNodeWithTag("loginButton").performClick()
        composeRule.waitUntil(3_000) {
            composeRule
                .onAllNodesWithText("Usuari o contrasenya incorrectes")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("Usuari o contrasenya incorrectes").assertIsDisplayed()
    }

    @Test
    fun login_with_correct_credentials_calls_onLoginSuccess() {
        val latch = java.util.concurrent.CountDownLatch(1)
        val viewModel = LoginViewModel(FakeAuthControllerSuccess())

        composeRule.setContent {
            LoginScreen(
                onLoginSuccess = { latch.countDown() },
                loginViewModel = viewModel
            )
        }

        composeRule.onNodeWithTag("usernameField").performTextInput("admin")
        composeRule.onNodeWithTag("passwordField").performTextInput("1234")
        composeRule.onNodeWithTag("loginButton").performClick()

        composeRule.waitForIdle()

        org.junit.Assert.assertTrue(
            "onLoginSuccess no s'ha cridat dins del temps esperat",
            latch.await(5, java.util.concurrent.TimeUnit.SECONDS)
        )
    }

}