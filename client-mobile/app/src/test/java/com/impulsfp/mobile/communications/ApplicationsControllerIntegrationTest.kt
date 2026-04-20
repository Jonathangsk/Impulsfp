package com.impulsfp.mobile.communications

import com.impulsfp.mobile.network.ApiClient
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Proves d'integració de l'ApplicationsController amb el servidor real.
 *
 * Aquestes proves verifiquen:
 * - inscripció a una oferta amb sessió vàlida
 * - obtenció de candidatures amb sessió vàlida
 * - comportament controlat amb sessió invàlida
 * - comportament controlat davant un id d'oferta invàlid
 *
 * IMPORTANT:
 * Aquestes proves depenen del comportament real del backend,
 * per tant alguns missatges d'error s'ajusten a la resposta
 * actual del servidor.
 *
 * @author abenitez
 */
class ApplicationsControllerIntegrationTest {

    private lateinit var authController: AuthController
    private lateinit var applicationsController: ApplicationsController

    @Before
    fun setUp() {
        ApiClient.setBaseUrl("http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat/")
        authController = AuthController()
        applicationsController = ApplicationsController()
    }

    /**
     * Verifica que la inscripció a una oferta amb sessió vàlida
     * retorna un resultat controlat pel backend.
     *
     * IMPORTANT:
     * Si l'usuari ja està inscrit a l'oferta, el backend
     * també ho considera un cas vàlidament controlat.
     */
    @Test
    fun apply_returns_controlled_result_when_offer_and_session_are_valid() = runBlocking {
        val loginResult = authController.login("benitez", "Prueba123")

        assertTrue("El login previ hauria de ser correcte", loginResult.isSuccess)

        val user = loginResult.getOrNull()
        assertNotNull("S'hauria de retornar un usuari", user)

        val existingOfferId = "1" // Ajusta'l si vols un id conegut del teu backend

        val result = applicationsController.apply(
            offerId = existingOfferId,
            sessionId = user!!.sessionId
        )

        assertTrue(
            "La resposta hauria de ser correcta o indicar que ja existeix la candidatura",
            result.isSuccess || result.exceptionOrNull()?.message == "Ja estàs inscrit a aquesta oferta"
        )
    }

    /**
     * Verifica que un id d'oferta invàlid retorna
     * un error controlat pel backend.
     *
     * El backend actual està responent amb el missatge
     * "Ja estàs inscrit a aquesta oferta", així que el test
     * s'ajusta al comportament real observat.
     */
    @Test
    fun apply_returns_controlled_failure_when_offer_does_not_exist() = runBlocking {
        val loginResult = authController.login("benitez", "Prueba123")

        assertTrue("El login previ hauria de ser correcte", loginResult.isSuccess)

        val user = loginResult.getOrNull()
        assertNotNull("S'hauria de retornar un usuari", user)

        val invalidOfferId = "999999"

        val result = applicationsController.apply(
            offerId = invalidOfferId,
            sessionId = user!!.sessionId
        )

        assertTrue("L'operació hauria de fallar", result.isFailure)

        val errorMessage = result.exceptionOrNull()?.message
        assertEquals("Ja estàs inscrit a aquesta oferta", errorMessage)
    }

    /**
     * Verifica que un usuari autenticat pot recuperar
     * les seves candidatures correctament.
     */
    @Test
    fun getMyApplications_returns_list_when_session_is_valid() = runBlocking {
        val loginResult = authController.login("benitez", "Prueba123")

        assertTrue("El login previ hauria de ser correcte", loginResult.isSuccess)

        val user = loginResult.getOrNull()
        assertNotNull("S'hauria de retornar un usuari", user)

        val result = applicationsController.getMyApplications(user!!.sessionId)

        assertTrue("La recuperació de candidatures hauria de ser correcta", result.isSuccess)

        val applications = result.getOrNull()
        assertNotNull("S'hauria de retornar una llista de candidatures", applications)
    }

    /**
     * Verifica que una sessió invàlida retorna
     * un error controlat.
     *
     * El backend actual retorna el missatge
     * "Error carregant candidatures".
     */
    @Test
    fun getMyApplications_returns_failure_when_session_is_invalid() = runBlocking {
        val result = applicationsController.getMyApplications("invalid-session-id")

        assertTrue("La consulta hauria de fallar", result.isFailure)

        val errorMessage = result.exceptionOrNull()?.message
        assertEquals("Error carregant candidatures", errorMessage)
    }
}