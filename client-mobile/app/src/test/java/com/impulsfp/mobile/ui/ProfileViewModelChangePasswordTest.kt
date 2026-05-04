package com.impulsfp.mobile.ui

import com.impulsfp.mobile.MainDispatcherRule
import com.impulsfp.mobile.communications.ProfileController
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ProfileViewModelChangePasswordTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    class SuccessChangePasswordController : ProfileController() {
        var changePasswordCalled = false
        var lastSessionId: String? = null
        var lastCurrentPassword: String? = null
        var lastNewPassword: String? = null

        override suspend fun changePassword(
            sessionId: String,
            currentPassword: String,
            newPassword: String
        ): Result<String> {
            changePasswordCalled = true
            lastSessionId = sessionId
            lastCurrentPassword = currentPassword
            lastNewPassword = newPassword
            return Result.success("Contrasenya actualitzada correctament")
        }
    }

    class FailingChangePasswordController : ProfileController() {
        var changePasswordCalled = false

        override suspend fun changePassword(
            sessionId: String,
            currentPassword: String,
            newPassword: String
        ): Result<String> {
            changePasswordCalled = true
            return Result.failure(Exception("Contrasenya actual incorrecta"))
        }
    }

    @Test
    fun changePassword_backendOk_cridaController_iExecutaOnSuccess() = runTest {
        val fakeController = SuccessChangePasswordController()
        val viewModel = ProfileViewModel(fakeController)

        var successMessage: String? = null
        var errorMessage: String? = null

        viewModel.changePassword(
            sessionId = "fake-session",
            currentPassword = "Password1",
            newPassword = "Password2",
            onSuccess = { successMessage = it },
            onError = { errorMessage = it }
        )

        advanceUntilIdle()

        assertTrue(fakeController.changePasswordCalled)
        assertEquals("fake-session", fakeController.lastSessionId)
        assertEquals("Password1", fakeController.lastCurrentPassword)
        assertEquals("Password2", fakeController.lastNewPassword)

        assertEquals("Contrasenya actualitzada correctament", successMessage)
        assertEquals(null, errorMessage)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun changePassword_backendFalla_cridaController_iExecutaOnError() = runTest {
        val fakeController = FailingChangePasswordController()
        val viewModel = ProfileViewModel(fakeController)

        var successMessage: String? = null
        var errorMessage: String? = null

        viewModel.changePassword(
            sessionId = "fake-session",
            currentPassword = "PasswordIncorrecta1",
            newPassword = "Password2",
            onSuccess = { successMessage = it },
            onError = { errorMessage = it }
        )

        advanceUntilIdle()

        assertTrue(fakeController.changePasswordCalled)

        assertEquals(null, successMessage)
        assertEquals("Contrasenya actual incorrecta", errorMessage)
        assertFalse(viewModel.isLoading)
    }
}