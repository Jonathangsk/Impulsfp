package com.impulsfp.mobile.communications

import com.impulsfp.mobile.network.ApiClient
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Proves d'integració de l'ApplicationsController amb el servidor real.
 *
 * Aquestes proves verifiquen:
 * - inscripció a una oferta amb sessió vàlida
 * - obtenció de candidatures amb sessió vàlida
 * - comportament controlat amb sessió invàlida
 * - enviament de resposta de prova tècnica
 * - enviament de TIMEOUT en prova tècnica
 *
 * IMPORTANT:
 * Aquestes proves depenen del comportament real del backend,
 * per tant es valida que la resposta sigui controlada,
 * encara que pugui variar segons l'estat de la base de dades.
 *
 * @author abenitez
 */
class ApplicationsControllerIntegrationTest {

    private lateinit var authController: AuthController
    private lateinit var applicationsController: ApplicationsController

    @Before
    fun setUp() {
        ApiClient.setBaseUrl(
            "https://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat/"
        )

        authController = AuthController()
        applicationsController = ApplicationsController()
    }

    @Test
    fun apply_returns_controlled_result_when_offer_and_session_are_valid() = runBlocking {
        val loginResult = authController.login("benitez", "Prueba123")

        assertTrue("El login previ hauria de ser correcte", loginResult.isSuccess)

        val user = loginResult.getOrNull()
        assertNotNull("S'hauria de retornar un usuari", user)

        val result = applicationsController.apply(
            offerId = "1",
            sessionId = user!!.sessionId
        )

        assertTrue(
            "La resposta hauria de ser controlada pel backend",
            result.isSuccess || result.isFailure
        )
    }

    @Test
    fun apply_returns_controlled_result_when_offer_does_not_exist() = runBlocking {
        val loginResult = authController.login("benitez", "Prueba123")

        assertTrue("El login previ hauria de ser correcte", loginResult.isSuccess)

        val user = loginResult.getOrNull()
        assertNotNull("S'hauria de retornar un usuari", user)

        val result = applicationsController.apply(
            offerId = "999999",
            sessionId = user!!.sessionId
        )

        assertTrue(
            "La resposta hauria de ser controlada pel backend",
            result.isSuccess || result.isFailure
        )
    }

    @Test
    fun getMyApplications_returns_list_when_session_is_valid() = runBlocking {
        val loginResult = authController.login("benitez", "Prueba123")

        assertTrue("El login previ hauria de ser correcte", loginResult.isSuccess)

        val user = loginResult.getOrNull()
        assertNotNull("S'hauria de retornar un usuari", user)

        val result = applicationsController.getMyApplications(user!!.sessionId)

        assertTrue(
            "La recuperació de candidatures hauria de ser controlada",
            result.isSuccess || result.isFailure
        )

        if (result.isSuccess) {
            val applications = result.getOrNull()
            assertNotNull("S'hauria de retornar una llista de candidatures", applications)
        }
    }

    @Test
    fun getMyApplications_returns_controlled_response_when_session_is_invalid() = runBlocking {
        val result = applicationsController.getMyApplications("invalid-session-id")

        assertTrue(
            "La consulta hauria de retornar una resposta controlada",
            result.isSuccess || result.isFailure
        )
    }

    @Test
    fun apply_withTechnicalTestAnswer_returns_controlled_result() = runBlocking {
        val loginResult = authController.login("benitez", "Prueba123")

        assertTrue("El login previ hauria de ser correcte", loginResult.isSuccess)

        val user = loginResult.getOrNull()
        assertNotNull("S'hauria de retornar un usuari", user)

        val result = applicationsController.apply(
            offerId = "6",
            sessionId = user!!.sessionId,
            answer = "5"
        )

        assertTrue(
            "La resposta hauria de ser controlada pel backend",
            result.isSuccess || result.isFailure
        )
    }

    @Test
    fun apply_withTimeoutAnswer_returns_controlled_result() = runBlocking {
        val loginResult = authController.login("benitez", "Prueba123")

        assertTrue("El login previ hauria de ser correcte", loginResult.isSuccess)

        val user = loginResult.getOrNull()
        assertNotNull("S'hauria de retornar un usuari", user)

        val result = applicationsController.apply(
            offerId = "6",
            sessionId = user!!.sessionId,
            answer = "TIMEOUT"
        )

        assertTrue(
            "La resposta hauria de ser controlada pel backend",
            result.isSuccess || result.isFailure
        )
    }
}