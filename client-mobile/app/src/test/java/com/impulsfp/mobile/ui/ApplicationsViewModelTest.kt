package com.impulsfp.mobile.ui

import com.impulsfp.mobile.MainDispatcherRule
import com.impulsfp.mobile.communications.ApplicationsController
import com.impulsfp.mobile.data.ApplicationUiModel
import com.impulsfp.mobile.data.SessionData
import com.impulsfp.mobile.data.User
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Tests unitaris de ApplicationsViewModel.
 *
 * Comproven la càrrega de candidatures i la gestió d'errors
 * sense utilitzar el servidor real.
 */
class ApplicationsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    /**
     * Controlador fals que simula una resposta correcta del backend.
     */
    class SuccessApplicationsController(
        private val applicationsToReturn: List<ApplicationUiModel>
    ) : ApplicationsController() {

        var getMyApplicationsCalled = false
        var receivedSessionId: String? = null

        override suspend fun getMyApplications(
            sessionId: String
        ): Result<List<ApplicationUiModel>> {
            getMyApplicationsCalled = true
            receivedSessionId = sessionId
            return Result.success(applicationsToReturn)
        }
    }

    /**
     * Controlador fals que simula un error del backend.
     */
    class FailureApplicationsController(
        private val errorMessage: String
    ) : ApplicationsController() {

        var getMyApplicationsCalled = false
        var receivedSessionId: String? = null

        override suspend fun getMyApplications(
            sessionId: String
        ): Result<List<ApplicationUiModel>> {
            getMyApplicationsCalled = true
            receivedSessionId = sessionId
            return Result.failure(Exception(errorMessage))
        }
    }

    /**
     * Llista de candidatures de prova reutilitzable als tests.
     */
    private fun sampleApplications(): List<ApplicationUiModel> {
        return listOf(
            ApplicationUiModel(
                id = 1,
                offerTitle = "Desenvolupador/a Android Junior",
                companyName = "TechNova",
                location = "Barcelona",
                status = "Enviada",
                appliedAt = "10/04/2026"
            ),
            ApplicationUiModel(
                id = 2,
                offerTitle = "Programador/a Backend Java",
                companyName = "InnovaSoft",
                location = "Girona",
                status = "En revisió",
                appliedAt = "08/04/2026"
            ),
            ApplicationUiModel(
                id = 3,
                offerTitle = "Tècnic/a de Sistemes",
                companyName = "CloudBase",
                location = "Tarragona",
                status = "Acceptada",
                appliedAt = "04/04/2026"
            )
        )
    }

    /**
     * Comprova que si no hi ha sessió activa,
     * no es crida backend i es mostra error.
     */
    @Test
    fun loadApplications_senseSessioActiva_mostraError() = runTest {
        // Arrange
        SessionData.currentUser = null
        val fakeController = SuccessApplicationsController(sampleApplications())

        // Act
        val viewModel = ApplicationsViewModel(fakeController)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value

        assertFalse(fakeController.getMyApplicationsCalled)
        assertEquals("No hi ha cap sessió activa", state.errorMessage)
        assertFalse(state.isLoading)
        assertTrue(state.applications.isEmpty())
    }

    /**
     * Comprova que si el backend respon correctament,
     * es carreguen les candidatures i desapareix el loading.
     */
    @Test
    fun loadApplications_backendOk_carregaCandidatures() = runTest {
        // Arrange
        SessionData.currentUser = User(
            username = "ramon",
            role = "ALUMNE",
            sessionId = "session-123"
        )

        val applications = sampleApplications()
        val fakeController = SuccessApplicationsController(applications)

        // Act
        val viewModel = ApplicationsViewModel(fakeController)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value

        assertTrue(fakeController.getMyApplicationsCalled)
        assertEquals("session-123", fakeController.receivedSessionId)
        assertFalse(state.isLoading)
        assertEquals(null, state.errorMessage)
        assertEquals(3, state.applications.size)
        assertEquals("Desenvolupador/a Android Junior", state.applications[0].offerTitle)
    }

    /**
     * Comprova que si el backend falla,
     * es mostra error i no hi ha candidatures carregades.
     */
    @Test
    fun loadApplications_backendFalla_mostraError() = runTest {
        // Arrange
        SessionData.currentUser = User(
            username = "ramon",
            role = "ALUMNE",
            sessionId = "session-123"
        )

        val fakeController = FailureApplicationsController("Error en carregar les candidatures")

        // Act
        val viewModel = ApplicationsViewModel(fakeController)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value

        assertTrue(fakeController.getMyApplicationsCalled)
        assertEquals("session-123", fakeController.receivedSessionId)
        assertFalse(state.isLoading)
        assertEquals("Error en carregar les candidatures", state.errorMessage)
        assertTrue(state.applications.isEmpty())
    }

    /**
     * Comprova que si el backend retorna una llista buida,
     * l'estat queda correctament carregat però sense candidatures.
     */
    @Test
    fun loadApplications_backendOk_llistaBuida() = runTest {
        // Arrange
        SessionData.currentUser = User(
            username = "ramon",
            role = "ALUMNE",
            sessionId = "session-123"
        )

        val fakeController = SuccessApplicationsController(emptyList())

        // Act
        val viewModel = ApplicationsViewModel(fakeController)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value

        assertTrue(fakeController.getMyApplicationsCalled)
        assertFalse(state.isLoading)
        assertEquals(null, state.errorMessage)
        assertTrue(state.applications.isEmpty())
    }

    @After
    fun tearDown() {
        SessionData.currentUser = null
    }
}