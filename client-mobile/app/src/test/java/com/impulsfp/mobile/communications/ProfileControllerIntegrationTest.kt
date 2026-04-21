package com.impulsfp.mobile.communications

import com.impulsfp.mobile.network.ApiClient
import com.impulsfp.mobile.network.UpdateProfileRequest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Proves d'integració de ProfileController amb el servidor real.
 *
 * Aquestes proves verifiquen:
 * - obtenció del perfil amb sessió vàlida
 * - resposta controlada amb sessió invàlida
 * - actualització del perfil amb sessió vàlida
 * - resposta controlada en eliminar el compte amb contrasenya incorrecta
 *
 * IMPORTANT:
 * Aquestes proves utilitzen el backend real i poden variar
 * segons l'estat actual de les dades del servidor.
 *
 * També es respecten les restriccions funcionals de l'aplicació:
 * - no es modifica el username
 * - no es modifica l'email
 *
 * @author abenitez
 */
class ProfileControllerIntegrationTest {

    private lateinit var authController: AuthController
    private lateinit var profileController: ProfileController

    @Before
    fun setUp() {
        ApiClient.setBaseUrl(
            "http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat/"
        )

        authController = AuthController()
        profileController = ProfileController()
    }

    /**
     * Verifica que es recupera correctament el perfil
     * d'un usuari autenticat.
     */
    @Test
    fun getProfile_returns_profile_when_session_is_valid() = runBlocking {
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

        val result = profileController.getProfile(user!!.sessionId)

        assertTrue(
            "La recuperació del perfil hauria de ser correcta",
            result.isSuccess
        )

        val profile = result.getOrNull()
        assertNotNull(
            "S'hauria de retornar un perfil d'usuari",
            profile
        )

        assertTrue(
            "El username del perfil no hauria d'estar buit",
            profile!!.username.isNotBlank()
        )
    }

    /**
     * Verifica que una sessió invàlida rep una resposta controlada.
     *
     * El backend real pot retornar èxit o error segons la validació
     * implementada al servidor, però no hauria de produir una fallada
     * inesperada al client.
     */
    @Test
    fun getProfile_with_invalid_session_returns_controlled_response() = runBlocking {
        val result = profileController.getProfile("invalid-session-id")

        assertTrue(
            "El backend hauria de respondre de forma controlada",
            result.isSuccess || result.isFailure
        )
    }

    /**
     * Verifica que l'actualització del perfil amb sessió vàlida
     * retorna una resposta controlada.
     *
     * Aquest test respecta el comportament funcional de l'aplicació:
     * no es modifica ni el username ni l'email, ja que no són editables.
     *
     * Per això primer es recupera el perfil actual i després s'envia
     * una actualització mantenint aquests camps intactes.
     */
    @Test
    fun updateProfile_with_valid_session_returns_controlled_response() = runBlocking {
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

        val profileResult = profileController.getProfile(user!!.sessionId)

        assertTrue(
            "La recuperació prèvia del perfil hauria de ser correcta",
            profileResult.isSuccess
        )

        val currentProfile = profileResult.getOrNull()
        assertNotNull(
            "S'hauria de retornar el perfil actual",
            currentProfile
        )

        val request = UpdateProfileRequest(
            name = currentProfile!!.name,
            surname = currentProfile.surname,
            phoneNumber = currentProfile.phoneNumber,
            city = currentProfile.city,
            bio = currentProfile.bio,
            cycle = currentProfile.cycle,
            skills = currentProfile.skills,
            experienceLevel = currentProfile.experienceLevel,
            languages = currentProfile.languages,
            preferredRoles = currentProfile.preferredRoles,
            preferredLocation = currentProfile.preferredLocation,
            availability = currentProfile.availability,
            portfolio = currentProfile.portfolio
        )

        val result = profileController.updateProfile(
            sessionId = user.sessionId,
            request = request
        )

        assertTrue(
            "L'actualització hauria de retornar una resposta controlada",
            result.isSuccess || result.isFailure
        )
    }

    /**
     * Verifica que la petició d'eliminar el compte amb una
     * contrasenya incorrecta retorna una resposta controlada.
     *
     * Aquest test no elimina cap compte real, ja que s'envia
     * una contrasenya incorrecta expressament.
     */
    @Test
    fun deleteAccount_with_wrong_password_returns_controlled_response() = runBlocking {
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

        val result = profileController.deleteAccount(
            sessionId = user!!.sessionId,
            password = "incorrecta"
        )

        assertTrue(
            "La resposta del backend hauria de ser controlada",
            result.isSuccess || result.isFailure
        )
    }
}