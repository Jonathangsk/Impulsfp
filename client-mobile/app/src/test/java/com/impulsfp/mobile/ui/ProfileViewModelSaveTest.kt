package com.impulsfp.mobile.ui

import com.impulsfp.mobile.MainDispatcherRule
import com.impulsfp.mobile.communications.ProfileController
import com.impulsfp.mobile.data.SessionData
import com.impulsfp.mobile.data.User
import com.impulsfp.mobile.network.UpdateProfileRequest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests unitaris del procés de desament del perfil
 * dins [ProfileViewModel].
 *
 * Aquestes proves verifiquen:
 * - validacions dels camps obligatoris
 * - comportament quan no hi ha sessió activa
 * - enviament correcte al backend
 * - tractament d'errors del backend
 * - actualització local de l'estat del perfil
 *
 * S'utilitzen controladors fake per simular respostes
 * correctes i errònies sense dependre del servidor real.
 *
 * @author abenitez
 */

class ProfileViewModelSaveTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        SessionData.clear()
    }

    class SuccessProfileController : ProfileController() {
        var updateProfileCalled = false
        var lastSessionId: String? = null
        var lastRequest: UpdateProfileRequest? = null

        override suspend fun updateProfile(
            sessionId: String,
            request: UpdateProfileRequest
        ): Result<String> {
            updateProfileCalled = true
            lastSessionId = sessionId
            lastRequest = request
            return Result.success("Perfil actualitzat")
        }
    }

    class FailingProfileController : ProfileController() {
        var updateProfileCalled = false

        override suspend fun updateProfile(
            sessionId: String,
            request: UpdateProfileRequest
        ): Result<String> {
            updateProfileCalled = true
            return Result.failure(Exception("Error backend"))
        }
    }

    private fun saveProfileWithValidData(
        viewModel: ProfileViewModel,
        onSuccess: () -> Unit = {}
    ) {
        viewModel.saveProfile(
            name = "Marc",
            surname = "Garcia",
            email = "marc@test.com",
            phoneNumber = "600111222",
            city = "Barcelona",
            bio = "Estudiant DAM",
            cycle = "DAM",
            skillsText = "Kotlin, SQL",
            experienceLevel = "Junior",
            languagesText = "Català, Castellà",
            preferredRolesText = "Android Developer, Backend Developer",
            preferredLocation = "Barcelona",
            availability = "Matins",
            portfolio = "https://portfolio.test",
            onSuccess = onSuccess
        )
    }

    @Test
    fun saveProfile_ambNameBuit_noCridaBackend_iMostraNameError() = runTest {
        val fakeController = SuccessProfileController()
        val viewModel = ProfileViewModel(fakeController)

        SessionData.currentUser = User(
            username = "user1",
            role = "ALUMNE",
            sessionId = "fake-session"
        )

        viewModel.saveProfile(
            name = "",
            surname = "Garcia",
            email = "marc@test.com",
            phoneNumber = "600111222",
            city = "Barcelona",
            bio = "Bio",
            cycle = "DAM",
            skillsText = "Kotlin, SQL",
            experienceLevel = "Junior",
            languagesText = "Català, Castellà",
            preferredRolesText = "Android Developer",
            preferredLocation = "Barcelona",
            availability = "Matins",
            portfolio = "https://portfolio.test",
            onSuccess = {}
        )

        assertEquals("El nom és obligatori.", viewModel.nameError)
        assertFalse(fakeController.updateProfileCalled)
    }

    @Test
    fun saveProfile_ambSurnameBuit_noCridaBackend_iMostraSurnameError() = runTest {
        val fakeController = SuccessProfileController()
        val viewModel = ProfileViewModel(fakeController)

        SessionData.currentUser = User(
            username = "user1",
            role = "ALUMNE",
            sessionId = "fake-session"
        )

        viewModel.saveProfile(
            name = "Marc",
            surname = "",
            email = "marc@test.com",
            phoneNumber = "600111222",
            city = "Barcelona",
            bio = "Bio",
            cycle = "DAM",
            skillsText = "Kotlin, SQL",
            experienceLevel = "Junior",
            languagesText = "Català, Castellà",
            preferredRolesText = "Android Developer",
            preferredLocation = "Barcelona",
            availability = "Matins",
            portfolio = "https://portfolio.test",
            onSuccess = {}
        )

        assertEquals("Els cognoms són obligatoris.", viewModel.surnameError)
        assertFalse(fakeController.updateProfileCalled)
    }

    @Test
    fun saveProfile_senseSessio_noCridaBackend_iMostraSaveError() = runTest {
        val fakeController = SuccessProfileController()
        val viewModel = ProfileViewModel(fakeController)

        SessionData.currentUser = null

        saveProfileWithValidData(viewModel)

        assertEquals("No hi ha cap sessió activa", viewModel.saveError)
        assertFalse(fakeController.updateProfileCalled)
    }

    @Test
    fun saveProfile_backendOk_actualitzaProfile_iActivaSaveSuccess() = runTest {
        val fakeController = SuccessProfileController()
        val viewModel = ProfileViewModel(fakeController)
        var successCalled = false

        SessionData.currentUser = User(
            username = "user1",
            role = "ALUMNE",
            sessionId = "fake-session"
        )

        saveProfileWithValidData(viewModel) {
            successCalled = true
        }

        advanceUntilIdle()

        assertTrue(fakeController.updateProfileCalled)
        assertEquals("fake-session", fakeController.lastSessionId)

        assertEquals("Marc", fakeController.lastRequest?.name)
        assertEquals("Garcia", fakeController.lastRequest?.surname)
        assertEquals("600111222", fakeController.lastRequest?.phoneNumber)
        assertEquals("Barcelona", fakeController.lastRequest?.city)
        assertEquals(listOf("Kotlin", "SQL"), fakeController.lastRequest?.skills)
        assertEquals(listOf("Català", "Castellà"), fakeController.lastRequest?.languages)
        assertEquals(
            listOf("Android Developer", "Backend Developer"),
            fakeController.lastRequest?.preferredRoles
        )

        assertEquals("Marc", viewModel.profile.name)
        assertEquals("Garcia", viewModel.profile.surname)
        assertEquals("Barcelona", viewModel.profile.city)
        assertEquals(listOf("Kotlin", "SQL"), viewModel.profile.skills)
        assertEquals(listOf("Català", "Castellà"), viewModel.profile.languages)
        assertEquals(
            listOf("Android Developer", "Backend Developer"),
            viewModel.profile.preferredRoles
        )

        assertTrue(viewModel.saveSuccess)
        assertNull(viewModel.saveError)
        assertFalse(viewModel.isLoading)
        assertTrue(successCalled)
    }

    @Test
    fun saveProfile_backendFalla_mostraSaveError_iNoActivaSaveSuccess() = runTest {
        val fakeController = FailingProfileController()
        val viewModel = ProfileViewModel(fakeController)
        var successCalled = false

        SessionData.currentUser = User(
            username = "user1",
            role = "ALUMNE",
            sessionId = "fake-session"
        )

        saveProfileWithValidData(viewModel) {
            successCalled = true
        }

        advanceUntilIdle()

        assertTrue(fakeController.updateProfileCalled)
        assertEquals("Error backend", viewModel.saveError)
        assertFalse(viewModel.saveSuccess)
        assertFalse(viewModel.isLoading)
        assertFalse(successCalled)
    }

    @Test
    fun saveProfile_ambCycleBuit_noCridaBackend_iMostraCycleError() = runTest {
        val fakeController = SuccessProfileController()
        val viewModel = ProfileViewModel(fakeController)

        SessionData.currentUser = User(
            username = "user1",
            role = "ALUMNE",
            sessionId = "fake-session"
        )

        viewModel.saveProfile(
            name = "Marc",
            surname = "Garcia",
            email = "marc@test.com",
            phoneNumber = "600111222",
            city = "Barcelona",
            bio = "Bio",
            cycle = "",
            skillsText = "Kotlin, SQL",
            experienceLevel = "Junior",
            languagesText = "Català, Castellà",
            preferredRolesText = "Android Developer",
            preferredLocation = "Barcelona",
            availability = "Matins",
            portfolio = "https://portfolio.test",
            onSuccess = {}
        )

        assertEquals("El cicle formatiu és obligatori.", viewModel.cycleError)
        assertFalse(fakeController.updateProfileCalled)
    }

    @Test
    fun saveProfile_ambCampsNoObligatorisBuits_guardaCorrectament() = runTest {
        val fakeController = SuccessProfileController()
        val viewModel = ProfileViewModel(fakeController)
        var successCalled = false

        SessionData.currentUser = User(
            username = "user1",
            role = "ALUMNE",
            sessionId = "fake-session"
        )

        viewModel.saveProfile(
            name = "Marc",
            surname = "Garcia",
            email = "marc@test.com",
            phoneNumber = "",
            city = "",
            bio = "",
            cycle = "DAM",
            skillsText = "",
            experienceLevel = "",
            languagesText = "",
            preferredRolesText = "",
            preferredLocation = "",
            availability = "",
            portfolio = "",
            onSuccess = {
                successCalled = true
            }
        )

        advanceUntilIdle()

        assertTrue(fakeController.updateProfileCalled)
        assertEquals("fake-session", fakeController.lastSessionId)

        assertEquals("Marc", fakeController.lastRequest?.name)
        assertEquals("Garcia", fakeController.lastRequest?.surname)
        assertEquals("", fakeController.lastRequest?.phoneNumber)
        assertEquals("", fakeController.lastRequest?.city)
        assertEquals("", fakeController.lastRequest?.bio)
        assertEquals("DAM", fakeController.lastRequest?.cycle)
        assertEquals("", fakeController.lastRequest?.experienceLevel)
        assertEquals("", fakeController.lastRequest?.preferredLocation)
        assertEquals("", fakeController.lastRequest?.availability)
        assertEquals("", fakeController.lastRequest?.portfolio)
        assertEquals(emptyList<String>(), fakeController.lastRequest?.skills)
        assertEquals(emptyList<String>(), fakeController.lastRequest?.languages)
        assertEquals(emptyList<String>(), fakeController.lastRequest?.preferredRoles)

        assertEquals("Marc", viewModel.profile.name)
        assertEquals("Garcia", viewModel.profile.surname)
        assertEquals("marc@test.com", viewModel.profile.email)
        assertEquals("", viewModel.profile.phoneNumber)
        assertEquals("", viewModel.profile.city)
        assertEquals("", viewModel.profile.bio)
        assertEquals("DAM", viewModel.profile.cycle)
        assertEquals("", viewModel.profile.experienceLevel)
        assertEquals("", viewModel.profile.preferredLocation)
        assertEquals("", viewModel.profile.availability)
        assertEquals("", viewModel.profile.portfolio)

        assertEquals(emptyList<String>(), viewModel.profile.skills)
        assertEquals(emptyList<String>(), viewModel.profile.languages)
        assertEquals(emptyList<String>(), viewModel.profile.preferredRoles)

        assertTrue(viewModel.saveSuccess)
        assertNull(viewModel.saveError)
        assertFalse(viewModel.isLoading)
        assertTrue(successCalled)
    }
}