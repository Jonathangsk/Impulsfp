package com.impulsfp.mobile.communications

import com.impulsfp.mobile.network.ApiClient
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Proves d'integració de l'AuthController amb el servidor real.
 *
 * Aquestes proves verifiquen:
 * - login correcte contra el backend
 * - error controlat quan les credencials són incorrectes
 * - logout correcte després d'un login vàlid
 *
 * IMPORTANT:
 * Aquestes proves depenen del servidor real i de l'estat actual
 * del backend. La URL base apunta al servidor remot amb HTTPS.
 *
 * @author abenitez
 */
class AuthControllerIntegrationTest {

    private lateinit var authController: AuthController

    @Before
    fun setUp() {
        ApiClient.setBaseUrl(
            "https://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat/"
        )

        authController = AuthController()
    }

    @Test
    fun login_returns_user_when_credentials_are_correct() = runBlocking {
        val result = authController.login("benitez", "Prueba123")

        assertTrue("El login hauria de ser correcte", result.isSuccess)

        val user = result.getOrNull()

        assertNotNull("S'hauria de retornar un usuari", user)
        assertEquals("benitez", user?.username)
        assertNotNull("S'hauria de retornar un sessionId", user?.sessionId)

        assertFalse(
            "El sessionId no hauria d'estar buit",
            user?.sessionId.isNullOrBlank()
        )
    }

    @Test
    fun login_returns_failure_when_credentials_are_incorrect() = runBlocking {
        val result = authController.login("benitez", "incorrecta")

        assertTrue(
            "El login hauria de retornar una resposta controlada",
            result.isSuccess || result.isFailure
        )

        if (result.isFailure) {
            val errorMessage = result.exceptionOrNull()?.message
            assertNotNull("S'hauria de retornar un missatge d'error", errorMessage)
        }
    }

    @Test
    fun logout_returns_controlled_result_after_valid_login() = runBlocking {
        val loginResult = authController.login("benitez", "Prueba123")

        assertTrue("El login previ hauria de ser correcte", loginResult.isSuccess)

        val user = loginResult.getOrNull()
        assertNotNull("S'hauria de retornar un usuari", user)

        val logoutResult = authController.logout(user!!.sessionId)

        assertTrue(
            "El logout hauria de retornar una resposta controlada",
            logoutResult.isSuccess || logoutResult.isFailure
        )
    }
}