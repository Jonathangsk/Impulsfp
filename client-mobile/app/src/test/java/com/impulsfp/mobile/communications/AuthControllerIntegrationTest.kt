package com.impulsfp.mobile.communications

import com.impulsfp.mobile.network.ApiClient
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Proves d'integració de l'AuthController amb el servidor real.
 *
 * Aquestes proves verifiquen:
 * - login correcte contra el backend
 * - error quan les credencials són incorrectes
 * - logout correcte després d'un login vàlid
 *
 * IMPORTANT: Aquestes proves s'executen en local, així que la URL base
 * s'ha de configurar a http://localhost:8080/
 *
 *  @author abenitez
 */
class AuthControllerIntegrationTest {

    private lateinit var authController: AuthController

    @Before
    fun setUp() {
        ApiClient.setBaseUrl("http://localhost:8080/")
        authController = AuthController()
    }

    @Test
    fun login_returns_user_when_credentials_are_correct() = runBlocking {
        val result = authController.login("Jonathan", "1234")

        assertTrue("El login hauria de ser correcte", result.isSuccess)

        val user = result.getOrNull()
        assertNotNull("S'hauria de retornar un usuari", user)
        assertEquals("Jonathan", user?.username)
        assertNotNull("S'hauria de retornar un sessionId", user?.sessionId)
        assertFalse(
            "El sessionId no hauria d'estar buit",
            user?.sessionId.isNullOrBlank()
        )
    }

    @Test
    fun login_returns_failure_when_credentials_are_incorrect() = runBlocking {
        val result = authController.login("Jonathan", "incorrecta")

        assertTrue("El login hauria de fallar", result.isFailure)

        val errorMessage = result.exceptionOrNull()?.message
        assertEquals("Usuari o contrasenya incorrectes", errorMessage)
    }

    @Test
    fun logout_returns_success_after_valid_login() = runBlocking {
        val loginResult = authController.login("Jonathan", "1234")

        assertTrue("El login previ hauria de ser correcte", loginResult.isSuccess)

        val user = loginResult.getOrNull()
        assertNotNull("S'hauria de retornar un usuari", user)

        val logoutResult = authController.logout(user!!.sessionId)

        println("logout isSuccess = ${logoutResult.isSuccess}")
        println("logout isFailure = ${logoutResult.isFailure}")
        println("logout error = ${logoutResult.exceptionOrNull()?.message}")

        assertTrue("El logout hauria de ser correcte", logoutResult.isSuccess)
    }
}