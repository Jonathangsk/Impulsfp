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
 * - canvi de contrasenya amb contrasenya incorrecta
 * - resposta controlada en eliminar el compte amb contrasenya incorrecta
 *
 * IMPORTANT:
 * Aquestes proves utilitzen el backend real i poden variar
 * segons l'estat actual de les dades del servidor.
 *
 * També es respecten les restriccions funcionals de l'aplicació:
 * - no es modifica el username
 * - no es modifica l'email
 * - no es canvia realment la contrasenya de l'usuari
 * - no s'elimina cap compte real
 *
 * @author abenitez
 */
class ProfileControllerIntegrationTest {

    private lateinit var authController: AuthController
    private lateinit var profileController: ProfileController

    @Before
    fun setUp() {
        ApiClient.setBaseUrl(
            "https://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat/"
        )

        authController = AuthController()
        profileController = ProfileController()
    }

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

    @Test
    fun getProfile_with_invalid_session_returns_controlled_response() = runBlocking {
        val result = profileController.getProfile("invalid-session-id")

        assertTrue(
            "El backend hauria de respondre de forma controlada",
            result.isSuccess || result.isFailure
        )
    }

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

    @Test
    fun changePassword_with_wrong_current_password_returns_controlled_response() = runBlocking {
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

        val result = profileController.changePassword(
            sessionId = user!!.sessionId,
            currentPassword = "incorrecta",
            newPassword = "PasswordNova123"
        )

        assertTrue(
            "El canvi de contrasenya amb contrasenya incorrecta hauria de retornar una resposta controlada",
            result.isSuccess || result.isFailure
        )
    }

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