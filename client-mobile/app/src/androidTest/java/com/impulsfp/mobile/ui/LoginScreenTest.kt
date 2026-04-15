package com.impulsfp.mobile.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.impulsfp.mobile.communications.AuthController
import com.impulsfp.mobile.data.User
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Tests instrumentats de la pantalla de login.
 *
 * Aquests tests comproven:
 * - la presència dels elements principals de la pantalla
 * - la validació quan els camps estan buits
 * - la visualització de l'error quan el login falla
 * - l'execució del callback quan el login és correcte
 *
 * Important:
 * aquests tests NO depenen del servidor real.
 * Per això s'utilitzen controladors falsos (fake) que simulen
 * una resposta correcta o incorrecta.
 */
class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    /**
     * Controlador fals que simula un login correcte.
     *
     * No consulta cap servidor ni cap base de dades real.
     * Sempre retorna èxit.
     */
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

    /**
     * Controlador fals que simula un login incorrecte.
     *
     * No consulta cap servidor ni cap base de dades real.
     * Sempre retorna error.
     */
    private class FakeAuthControllerFailure : AuthController() {
        override suspend fun login(username: String, password: String): Result<User> {
            return Result.failure(Exception("Usuari o contrasenya incorrectes"))
        }
    }

    /**
     * Comprova que la pantalla de login mostra els elements bàsics.
     */
    @Test
    fun login_screen_displays_basic_elements() {
        composeRule.setContent {
            @Suppress("ViewModelConstructorInComposable")
            LoginScreen(
                onLoginSuccess = {},
                onRegisterClick = {},
                loginViewModel = LoginViewModel()
            )
        }

        composeRule.onNodeWithText("Usuari").assertExists()
        composeRule.onNodeWithText("Contrasenya").assertExists()
        composeRule.onNodeWithText("Iniciar Sessió").assertExists()
        composeRule.onNodeWithText("No tens compte? Registra’t").assertExists()
    }

    /**
     * Comprova que si l'usuari prem el botó de login
     * amb els camps buits, es mostra el missatge d'error corresponent.
     */
    @Test
    fun login_with_empty_fields_show_error_message() {
        composeRule.setContent {
            @Suppress("ViewModelConstructorInComposable")
            LoginScreen(
                onLoginSuccess = {},
                onRegisterClick = {},
                loginViewModel = LoginViewModel()
            )
        }

        composeRule.onNodeWithTag("loginButton").performClick()

        composeRule.onNodeWithText(
            "Cal introduir l'usuari i la contrasenya per iniciar sessió."
        ).assertIsDisplayed()
    }

    /**
     * Comprova que si el backend simulat retorna error,
     * es mostra el missatge d'error a la pantalla.
     *
     * Les credencials escrites aquí són fictícies.
     * El resultat depèn del FakeAuthControllerFailure, no del servidor real.
     */
    @Test
    fun login_with_wrong_credentials_shows_error_message() {
        composeRule.setContent {
            @Suppress("ViewModelConstructorInComposable")
            LoginScreen(
                onLoginSuccess = {},
                onRegisterClick = {},
                loginViewModel = LoginViewModel(FakeAuthControllerFailure())
            )
        }

        composeRule.onNodeWithTag("usernameField").performTextInput("usuari_fake")
        composeRule.onNodeWithTag("passwordField").performTextInput("contrasenya_fake")
        composeRule.onNodeWithTag("loginButton").performClick()

        composeRule.waitUntil(3_000) {
            composeRule
                .onAllNodesWithText("Usuari o contrasenya incorrectes")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeRule.onNodeWithText("Usuari o contrasenya incorrectes").assertIsDisplayed()
    }

    /**
     * Comprova que si el backend simulat retorna èxit,
     * s'executa el callback onLoginSuccess.
     *
     * Les credencials escrites aquí són fictícies.
     * El resultat depèn del FakeAuthControllerSuccess, no del servidor real.
     */
    @Test
    fun login_with_correct_credentials_calls_onLoginSuccess() {
        val latch = CountDownLatch(1)
        val viewModel = LoginViewModel(FakeAuthControllerSuccess())

        composeRule.setContent {
            LoginScreen(
                onLoginSuccess = { latch.countDown() },
                onRegisterClick = {},
                loginViewModel = viewModel
            )
        }

        composeRule.onNodeWithTag("usernameField").performTextInput("usuari_fake")
        composeRule.onNodeWithTag("passwordField").performTextInput("Password1")
        composeRule.onNodeWithTag("loginButton").performClick()

        composeRule.waitUntil(5_000) {
            latch.count == 0L
        }

        assertTrue(
            "onLoginSuccess no s'ha cridat dins del temps esperat",
            latch.await(5, TimeUnit.SECONDS)
        )
    }
}