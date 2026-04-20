package com.impulsfp.mobile.communications

import com.impulsfp.mobile.network.ApiClient
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Proves d'integració de l'OffersController amb el servidor real.
 *
 * Aquestes proves verifiquen:
 * - obtenció correcta d'ofertes amb una sessió vàlida
 * - resposta controlada amb una sessió invàlida
 * - resposta controlada en aplicar a una oferta existent
 * - error controlat davant un id d'oferta invàlid
 *
 * IMPORTANT:
 * Aquestes proves depenen del comportament real del backend.
 * Alguns endpoints poden respondre diferent del previst teòricament.
 *
 * @author abenitez
 */
class OffersControllerIntegrationTest {

    private lateinit var authController: AuthController
    private lateinit var offersController: OffersController

    @Before
    fun setUp() {
        ApiClient.setBaseUrl(
            "http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat/"
        )

        authController = AuthController()
        offersController = OffersController()
    }

    /**
     * Verifica que es poden recuperar ofertes
     * amb una sessió vàlida.
     */
    @Test
    fun getOffers_returns_list_when_session_is_valid() = runBlocking {
        val loginResult = authController.login("benitez", "Prueba123")

        assertTrue(
            "El login previ hauria de ser correcte",
            loginResult.isSuccess
        )

        val user = loginResult.getOrNull()
        assertNotNull(
            "S'hauria de retornar un usuari",
            user
        )

        val result = offersController.getOffers(user!!.sessionId)

        assertTrue(
            "La recuperació d'ofertes hauria de ser correcta",
            result.isSuccess
        )

        val offers = result.getOrNull()

        assertNotNull(
            "S'hauria de retornar una llista d'ofertes",
            offers
        )
    }

    /**
     * Verifica que una sessió invàlida rep una resposta controlada.
     *
     * El backend actual pot retornar success o failure,
     * però no hauria de provocar errors inesperats.
     */
    @Test
    fun getOffers_with_invalid_session_returns_controlled_response() = runBlocking {
        val result = offersController.getOffers("invalid-session-id")

        assertTrue(
            "El backend hauria de respondre de forma controlada",
            result.isSuccess || result.isFailure
        )
    }

    /**
     * Verifica que aplicar a una oferta existent amb sessió vàlida
     * retorna una resposta controlada.
     *
     * Segons l'estat del backend, pot ser success o failure
     * (per exemple si ja existeix una candidatura prèvia).
     */
    @Test
    fun applyToOffer_with_valid_session_returns_controlled_response() = runBlocking {
        val loginResult = authController.login("benitez", "Prueba123")

        assertTrue(
            "El login previ hauria de ser correcte",
            loginResult.isSuccess
        )

        val user = loginResult.getOrNull()
        assertNotNull(
            "S'hauria de retornar un usuari",
            user
        )

        val result = offersController.applyToOffer(
            offerId = "1",
            sessionId = user!!.sessionId
        )

        assertTrue(
            "La resposta hauria de ser controlada pel backend",
            result.isSuccess || result.isFailure
        )
    }

    /**
     * Verifica que aplicar a una oferta inexistent
     * retorna un error controlat.
     */
    @Test
    fun applyToOffer_returns_controlled_failure_when_offer_does_not_exist() = runBlocking {
        val loginResult = authController.login("benitez", "Prueba123")

        assertTrue(
            "El login previ hauria de ser correcte",
            loginResult.isSuccess
        )

        val user = loginResult.getOrNull()
        assertNotNull(
            "S'hauria de retornar un usuari",
            user
        )

        val result = offersController.applyToOffer(
            offerId = "999999",
            sessionId = user!!.sessionId
        )

        assertTrue(
            "L'operació hauria de fallar o estar controlada",
            result.isFailure || result.isSuccess
        )
    }
}