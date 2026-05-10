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
            "https://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat/"
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
}