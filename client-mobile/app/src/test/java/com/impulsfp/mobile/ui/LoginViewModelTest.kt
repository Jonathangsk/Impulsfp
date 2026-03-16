package com.impulsfp.mobile.ui

import com.impulsfp.mobile.data.SessionData
import com.impulsfp.mobile.ui.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        SessionData.logout()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        SessionData.logout()
    }

    @Test
    fun validateCredentials_returnsError_whenUsernameAndPasswordAreBlank() {
        val viewModel = LoginViewModel()
        val result = viewModel.validateCredentials("","")

        assertEquals(
            "Cal introduir l'usuari i la contrasenya per iniciar sessió.",
            result
        )
    }

    @Test
    fun validateCredentials_returnsError_whenUsernameIsBlank() {
        val viewModel = LoginViewModel()
        val result = viewModel.validateCredentials("", "1234")

        assertEquals("Cal introduir el nom d'usuari.", result)
    }

    @Test
    fun validateCredentials_returnsError_whenPasswordIsBlank() {
        val viewModel = LoginViewModel()
        val result = viewModel.validateCredentials("admin", "")

        assertEquals("Cal introduir la contrasenya.", result)
    }

    @Test
    fun validateCredentials_returnsNull_whenCredentialsAreValid() {
        val viewModel = LoginViewModel()
        val result = viewModel.validateCredentials("admin", "1234")

        assertNull(result)
    }

    @Ignore("Depèn de credencials fake. S'adaptarà quan hi hagi backend real.")
    @Test
    fun login_updatesUiStateAndSession_whenCredentialsAreCorrect() = runTest {
        val viewModel = LoginViewModel()

        viewModel.onUsernameChange("admin")
        viewModel.onPasswordChange("1234")
        viewModel.login()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.loginSuccess)
        assertNull(state.errorMessage)

        assertNotNull(SessionData.currentUser)
        assertEquals("admin", SessionData.currentUser?.username)
        assertEquals("ADMIN", SessionData.currentUser?.role)
    }

    @Ignore("Dependrà del missatge que retorni el backend real")
    @Test
    fun login_showsError_whenCredentialsAreIncorrect() = runTest {
        val viewModel = LoginViewModel()

        viewModel.onUsernameChange("pep")
        viewModel.onPasswordChange("9999")

        viewModel.login()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.loginSuccess)
        assertEquals("Credencials incorrectes", state.errorMessage)
        assertNull(SessionData.currentUser)
    }

    @Test
    fun login_showsValidationError_whenFieldsAreBlank() = runTest {
        val viewModel = LoginViewModel()

        viewModel.onUsernameChange("")
        viewModel.onPasswordChange("")

        viewModel.login()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.loginSuccess)
        assertEquals(
            "Cal introduir l'usuari i la contrasenya per iniciar sessió.",
            state.errorMessage
        )
        assertNull(SessionData.currentUser)
    }

    @Ignore("Depèn de credencials fake. S'adaptarà quan hi hagi backend real.")
    @Test
    fun resetLoginSuccess_setsLoginSuccessToFalse() = runTest {
        val viewModel = LoginViewModel()

        viewModel.onUsernameChange("admin")
        viewModel.onPasswordChange("1234")
        viewModel.login()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.loginSuccess)

        viewModel.resetLoginSuccess()

        assertFalse(viewModel.uiState.value.loginSuccess)
    }
}
