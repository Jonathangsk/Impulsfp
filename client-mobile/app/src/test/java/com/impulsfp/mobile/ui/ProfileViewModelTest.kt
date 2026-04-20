package com.impulsfp.mobile.ui

import com.impulsfp.mobile.MainDispatcherRule
import com.impulsfp.mobile.communications.ProfileController
import com.impulsfp.mobile.data.UserProfile
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
/**
 * Tests unitaris de càrrega del perfil dins [ProfileViewModel].
 *
 * Aquestes proves verifiquen:
 * - recuperació correcta del perfil des del backend
 * - tractament d'errors de servidor
 * - actualització de l'estat loading
 * - integritat de les dades locals
 *
 * S'utilitzen controladors fake per evitar dependència
 * del backend real.
 *
 * @author abenitez
 */
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    /**
     * Controlador fake que simula una càrrega correcta del perfil.
     */
    class SuccessProfileController : ProfileController() {

        var getProfileCalled = false

        override suspend fun getProfile(sessionId: String): Result<UserProfile> {
            getProfileCalled = true

            return Result.success(
                UserProfile(
                    username = "alumne1",
                    name = "Marc",
                    surname = "Garcia",
                    email = "marc@test.com",
                    phoneNumber = "600111222",
                    city = "Barcelona",
                    bio = "Estudiant DAM",
                    cycle = "DAM",
                    skills = listOf("Kotlin", "SQL"),
                    experienceLevel = "Junior",
                    languages = listOf("Català", "Castellà"),
                    preferredRoles = listOf("Android Developer"),
                    preferredLocation = "Barcelona",
                    availability = "Matins",
                    portfolio = "https://portfolio.test"
                )
            )
        }
    }

    /**
     * Controlador fake que simula error del backend.
     */
    class FailingProfileController : ProfileController() {

        var getProfileCalled = false

        override suspend fun getProfile(sessionId: String): Result<UserProfile> {
            getProfileCalled = true
            return Result.failure(Exception("Error backend"))
        }
    }

    @Test
    fun refreshProfile_backendOk_ompleProfile_iTreuLoading_iNoDeixaError() = runTest {
        // Arrange
        val fakeController = SuccessProfileController()
        val viewModel = ProfileViewModel(fakeController)

        // Act
        viewModel.refreshProfile("fake-session")
        advanceUntilIdle()

        // Assert
        assertTrue(fakeController.getProfileCalled)

        assertEquals("alumne1", viewModel.profile.username)
        assertEquals("Marc", viewModel.profile.name)
        assertEquals("Garcia", viewModel.profile.surname)
        assertEquals("marc@test.com", viewModel.profile.email)
        assertEquals("DAM", viewModel.profile.cycle)

        assertEquals(listOf("Kotlin", "SQL"), viewModel.profile.skills)
        assertEquals(listOf("Català", "Castellà"), viewModel.profile.languages)

        assertFalse(viewModel.isLoading)
        assertEquals(null, viewModel.serverError)
    }

    @Test
    fun refreshProfile_backendFalla_ompleServerError_iTreuLoading_iNoTrencaProfile() = runTest {
        // Arrange
        val fakeController = FailingProfileController()
        val viewModel = ProfileViewModel(fakeController)

        val initialProfile = viewModel.profile

        // Act
        viewModel.refreshProfile("fake-session")
        advanceUntilIdle()

        // Assert
        assertTrue(fakeController.getProfileCalled)

        assertEquals("Error backend", viewModel.serverError)
        assertFalse(viewModel.isLoading)

        // comprovem que el perfil no queda corrupte
        assertEquals(initialProfile, viewModel.profile)
    }
}