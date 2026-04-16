package com.impulsfp.mobile.ui

import com.impulsfp.mobile.MainDispatcherRule
import com.impulsfp.mobile.communications.AuthController
import com.impulsfp.mobile.data.SessionData
import com.impulsfp.mobile.data.User
import com.impulsfp.mobile.network.RegisterRequest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Tests unitaris del RegisterViewModel.
 *
 * Aquests tests comproven la lògica interna del ViewModel
 * sense utilitzar servidor real ni fer crides de xarxa.
 */
class RegisterViewModelTest {

    /**
     * Regla que substitueix Dispatchers.Main per un dispatcher de test.
     *
     * És necessària perquè el ViewModel utilitza viewModelScope.launch { ... }.
     */
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    /**
     * Implementació falsa d'AuthController per als tests.
     *
     * No fa cap crida real al backend.
     * Serveix per simular un registre correcte i per saber
     * si registerStudent() s'ha arribat a executar.
     */
    class TrackingAuthController : AuthController() {

        /**
         * Indica si s'ha intentat fer el registre.
         */
        var registerCalled = false

        /**
         * Simula una resposta correcta del backend.
         *
         * @param request dades rebudes del formulari
         * @return usuari fictici creat correctament
         */
        override suspend fun registerStudent(
            request: RegisterRequest
        ): Result<User> {
            registerCalled = true

            return Result.success(
                User(
                    username = request.username,
                    role = "ALUMNE",
                    sessionId = "fake-session"
                )
            )
        }
    }

    /**
     * Implementació falsa d'AuthController que simula un error del backend.
     */
    class FailingAuthController : AuthController() {

        /**
         * Simula una resposta fallida del backend.
         *
         * @param request dades rebudes del formulari
         * @return error fictici de backend
         */
        override suspend fun registerStudent(request: RegisterRequest): Result<User> {
            return Result.failure(Exception("Error backend"))
        }
    }

    /**
     * Omple els camps obligatoris del formulari amb dades vàlides.
     *
     * Aquesta funció evita repetir el mateix codi en diversos tests
     * i fa que siguin més fàcils de llegir i mantenir.
     *
     * @param viewModel ViewModel sobre el qual volem carregar dades correctes
     */
    private fun fillValidRequiredFields(viewModel: RegisterViewModel) {
        viewModel.onUsernameChange("user123")
        viewModel.onNameChange("Marc")
        viewModel.onSurnameChange("Garcia")
        viewModel.onEmailChange("marc@test.com")
        viewModel.onPasswordChange("Password1")
        viewModel.onConfirmPasswordChange("Password1")
    }

    /**
     * Comprova que si l'usuari prem "Registrar-se"
     * amb els camps obligatoris buits:
     *
     * - es mostren els errors de validació
     * - no es crida el backend
     */
    @Test
    fun register_ambCampsObligatorisBuits_noCridaBackend_iMostraErrors() = runTest {

        // Arrange:
        // preparem el controlador fals i el ViewModel
        val fakeAuthController = TrackingAuthController()
        val viewModel = RegisterViewModel(fakeAuthController)

        // Act:
        // intentem registrar sense omplir cap camp
        viewModel.register()

        val state = viewModel.uiState.value

        // Assert:
        // comprovem errors dels camps obligatoris
        assertEquals(
            "El nom d'usuari és obligatori.",
            state.usernameError
        )

        assertEquals(
            "El nom és obligatori.",
            state.nameError
        )

        assertEquals(
            "Els cognoms són obligatoris.",
            state.surnameError
        )

        assertEquals(
            "El correu electrònic és obligatori.",
            state.emailError
        )

        assertEquals(
            "La contrasenya és obligatòria.",
            state.passwordError
        )

        assertEquals(
            "Has de confirmar la contrasenya.",
            state.confirmPasswordError
        )

        // També comprovem que NO s'ha cridat el backend
        assertFalse(fakeAuthController.registerCalled)
    }

    /**
     * Comprova que si el nom d'usuari és invàlid:
     *
     * - es mostra error de username
     * - no es crida el backend
     *
     * En aquest cas utilitzem caràcters no permesos.
     */
    @Test
    fun register_ambUsernameInvalid_noCridaBackend_iMostraErrorUsername() = runTest {

        // Arrange
        val fakeAuthController = TrackingAuthController()
        val viewModel = RegisterViewModel(fakeAuthController)

        // Omplim la resta de camps correctament
        fillValidRequiredFields(viewModel)
        viewModel.onUsernameChange("abc!")

        // Act
        viewModel.register()

        val state = viewModel.uiState.value

        // Assert
        assertEquals(
            "Només pot contenir lletres i números.",
            state.usernameError
        )

        assertFalse(fakeAuthController.registerCalled)
    }

    /**
     * Comprova que si el correu electrònic no és vàlid:
     *
     * - es mostra error d'email
     * - no es crida el backend
     */
    @Test
    fun register_ambEmailInvalid_noCridaBackend_iMostraErrorEmail() = runTest {

        // Arrange
        val fakeAuthController = TrackingAuthController()
        val viewModel = RegisterViewModel(fakeAuthController)

        // Omplim la resta de camps correctament
        fillValidRequiredFields(viewModel)
        viewModel.onEmailChange("correu_invalid")

        // Act
        viewModel.register()

        val state = viewModel.uiState.value

        // Assert
        assertEquals(
            "El correu electrònic no és vàlid.",
            state.emailError
        )

        assertFalse(fakeAuthController.registerCalled)
    }

    /**
     * Comprova que si la contrasenya no compleix els requisits:
     *
     * - es mostra error de contrasenya
     * - no es crida el backend
     */
    @Test
    fun register_ambPasswordInvalid_noCridaBackend_iMostraErrorPassword() = runTest {

        // Arrange
        val fakeAuthController = TrackingAuthController()
        val viewModel = RegisterViewModel(fakeAuthController)

        // Omplim la resta de camps correctament
        fillValidRequiredFields(viewModel)
        viewModel.onPasswordChange("abc")
        viewModel.onConfirmPasswordChange("abc")

        // Act
        viewModel.register()

        val state = viewModel.uiState.value

        // Assert
        assertNotNull(state.passwordError)
        assertTrue(state.passwordError!!.contains("La contrasenya ha de contenir:"))
        assertFalse(fakeAuthController.registerCalled)
    }

    /**
     * Comprova que si la confirmació de contrasenya
     * no coincideix amb la contrasenya:
     *
     * - es mostra error de confirmació
     * - no es crida el backend
     */
    @Test
    fun register_ambConfirmPasswordDiferent_noCridaBackend_iMostraErrorConfirmPassword() = runTest {

        // Arrange
        val fakeAuthController = TrackingAuthController()
        val viewModel = RegisterViewModel(fakeAuthController)

        // Omplim la resta de camps correctament
        fillValidRequiredFields(viewModel)
        viewModel.onConfirmPasswordChange("Password2")

        // Act
        viewModel.register()

        val state = viewModel.uiState.value

        // Assert
        assertEquals(
            "Les contrasenyes no coincideixen.",
            state.confirmPasswordError
        )

        assertFalse(fakeAuthController.registerCalled)
    }

    /**
     * Comprova que si totes les dades són correctes:
     *
     * - es crida el backend
     * - no hi ha errors de validació
     */
    @Test
    fun register_ambDadesCorrectes_cridaBackend() = runTest {

        // Arrange
        val fakeAuthController = TrackingAuthController()
        val viewModel = RegisterViewModel(fakeAuthController)

        fillValidRequiredFields(viewModel)

        // Act
        viewModel.register()

        // Esperem que acabi la coroutine
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value

        assertTrue(fakeAuthController.registerCalled)

        assertEquals(null, state.usernameError)
        assertEquals(null, state.nameError)
        assertEquals(null, state.surnameError)
        assertEquals(null, state.emailError)
        assertEquals(null, state.passwordError)
        assertEquals(null, state.confirmPasswordError)
    }

    /**
     * Comprova que si el backend respon correctament:
     *
     * - SessionData.currentUser s'omple
     * - registerSuccess passa a true
     * - isLoading torna a false
     */
    @Test
    fun register_backendOk_ompleSessionData_iActivaRegisterSuccess() = runTest {

        // Arrange
        SessionData.currentUser = null

        val fakeAuthController = TrackingAuthController()
        val viewModel = RegisterViewModel(fakeAuthController)

        fillValidRequiredFields(viewModel)

        // Act
        viewModel.register()
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value

        assertEquals("user123", SessionData.currentUser?.username)
        assertEquals("ALUMNE", SessionData.currentUser?.role)
        assertEquals("fake-session", SessionData.currentUser?.sessionId)

        assertTrue(state.registerSuccess)
        assertFalse(state.isLoading)
    }

    /**
     * Comprova que si el backend falla:
     *
     * - no s'activa registerSuccess
     * - isLoading torna a false
     * - es mostra serverError
     * - SessionData.currentUser continua buit
     */
    @Test
    fun register_backendFalla_noActivaRegisterSuccess_iMostraServerError() = runTest {

        // Arrange
        SessionData.currentUser = null

        val fakeAuthController = FailingAuthController()
        val viewModel = RegisterViewModel(fakeAuthController)

        fillValidRequiredFields(viewModel)

        // Act
        viewModel.register()
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value

        assertFalse(state.registerSuccess)
        assertFalse(state.isLoading)
        assertEquals("Error backend", state.serverError)
        assertEquals(null, SessionData.currentUser)
    }
}