package com.impulsfp.mobile.ui

import com.impulsfp.mobile.MainDispatcherRule
import com.impulsfp.mobile.communications.OffersController
import com.impulsfp.mobile.data.Offer
import com.impulsfp.mobile.data.SessionData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Tests unitaris de OffersViewModel.
 *
 * Comproven la càrrega d'ofertes i la lògica de cerca
 * sense utilitzar el servidor real.
 */
class OffersViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    /**
     * Controlador fals que simula una resposta correcta del backend.
     */
    class SuccessOffersController(
        private val offersToReturn: List<Offer>
    ) : OffersController() {

        var getOffersCalled = false
        var receivedSessionId: String? = null

        override suspend fun getOffers(sessionId: String): Result<List<Offer>> {
            getOffersCalled = true
            receivedSessionId = sessionId
            return Result.success(offersToReturn)
        }
    }

    /**
     * Controlador fals que simula un error del backend.
     */
    class FailureOffersController(
        private val errorMessage: String
    ) : OffersController() {

        var getOffersCalled = false
        var receivedSessionId: String? = null

        override suspend fun getOffers(sessionId: String): Result<List<Offer>> {
            getOffersCalled = true
            receivedSessionId = sessionId
            return Result.failure(Exception(errorMessage))
        }
    }

    /**
     * Llista d'ofertes de prova reutilitzable als tests.
     */
    private fun sampleOffers(): List<Offer> {
        return listOf(
            Offer(
                id = "1",
                title = "Desenvolupador Android",
                description = "Apps Android amb Kotlin",
                company = "Tech Solutions",
                requiredSkills = listOf("Kotlin", "Android", "Jetpack"),
                location = "Barcelona",
                modality = "Híbrid",
                contractType = "Pràctiques",
                salary = null,
                createdAt = "2026-03-31",
                applicantsCount = 5
            ),
            Offer(
                id = "2",
                title = "Backend Developer",
                description = "APIs REST i bases de dades",
                company = "DataCorp",
                requiredSkills = listOf("Java", "Spring", "SQL"),
                location = "Remot",
                modality = "Remot",
                contractType = "Pràctiques",
                salary = null,
                createdAt = "2026-03-05",
                applicantsCount = 3
            ),
            Offer(
                id = "3",
                title = "Frontend Developer",
                description = "Desenvolupament amb React",
                company = "Webify",
                requiredSkills = listOf("React", "JavaScript", "CSS"),
                location = "Girona",
                modality = "Presencial",
                contractType = "Pràctiques",
                salary = null,
                createdAt = "2026-03-10",
                applicantsCount = 2
            )
        )
    }

    /**
     * Comprova que si no hi ha sessió activa,
     * no es crida backend i es mostra error.
     */
    @Test
    fun loadOffers_senseSessioActiva_mostraError() = runTest {
        // Arrange
        SessionData.currentUser = null
        val fakeController = SuccessOffersController(sampleOffers())

        // Act
        val viewModel = OffersViewModel(fakeController)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value

        assertFalse(fakeController.getOffersCalled)
        assertEquals("No hi ha cap sessió activa", state.errorMessage)
        assertFalse(state.isLoading)
        assertTrue(state.offers.isEmpty())
        assertTrue(state.filteredOffers.isEmpty())
    }

    /**
     * Comprova que si el backend respon correctament,
     * es carreguen les ofertes i desapareix el loading.
     */
    @Test
    fun loadOffers_backendOk_carregaOfertes() = runTest {
        // Arrange
        SessionData.currentUser = com.impulsfp.mobile.data.User(
            username = "ramon",
            role = "ALUMNE",
            sessionId = "session-123"
        )

        val offers = sampleOffers()
        val fakeController = SuccessOffersController(offers)

        // Act
        val viewModel = OffersViewModel(fakeController)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value

        assertTrue(fakeController.getOffersCalled)
        assertEquals("session-123", fakeController.receivedSessionId)
        assertFalse(state.isLoading)
        assertEquals(null, state.errorMessage)
        assertEquals(3, state.offers.size)
        assertEquals(3, state.filteredOffers.size)
    }

    /**
     * Comprova que si el backend falla,
     * es mostra error i no hi ha ofertes carregades.
     */
    @Test
    fun loadOffers_backendFalla_mostraError() = runTest {
        // Arrange
        SessionData.currentUser = com.impulsfp.mobile.data.User(
            username = "ramon",
            role = "ALUMNE",
            sessionId = "session-123"
        )

        val fakeController = FailureOffersController("Error en carregar les ofertes")

        // Act
        val viewModel = OffersViewModel(fakeController)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value

        assertTrue(fakeController.getOffersCalled)
        assertFalse(state.isLoading)
        assertEquals("Error en carregar les ofertes", state.errorMessage)
        assertTrue(state.offers.isEmpty())
        assertTrue(state.filteredOffers.isEmpty())
    }

    /**
     * Comprova que una cerca buida retorna totes les ofertes.
     */
    @Test
    fun onSearchQueryChange_queryBuida_mostraTotesLesOfertes() = runTest {
        // Arrange
        SessionData.currentUser = com.impulsfp.mobile.data.User(
            username = "ramon",
            role = "ALUMNE",
            sessionId = "session-123"
        )

        val fakeController = SuccessOffersController(sampleOffers())
        val viewModel = OffersViewModel(fakeController)
        advanceUntilIdle()

        // Act
        viewModel.onSearchQueryChange("")

        // Assert
        val state = viewModel.uiState.value
        assertEquals(3, state.filteredOffers.size)
    }

    /**
     * Comprova que la cerca filtra per títol.
     */
    @Test
    fun onSearchQueryChange_filtraPerTitol() = runTest {
        // Arrange
        SessionData.currentUser = com.impulsfp.mobile.data.User(
            username = "ramon",
            role = "ALUMNE",
            sessionId = "session-123"
        )

        val fakeController = SuccessOffersController(sampleOffers())
        val viewModel = OffersViewModel(fakeController)
        advanceUntilIdle()

        // Act
        viewModel.onSearchQueryChange("Android")

        // Assert
        val state = viewModel.uiState.value
        assertEquals(1, state.filteredOffers.size)
        assertEquals("Desenvolupador Android", state.filteredOffers.first().title)
    }

    /**
     * Comprova que la cerca filtra per empresa.
     */
    @Test
    fun onSearchQueryChange_filtraPerEmpresa() = runTest {
        // Arrange
        SessionData.currentUser = com.impulsfp.mobile.data.User(
            username = "ramon",
            role = "ALUMNE",
            sessionId = "session-123"
        )

        val fakeController = SuccessOffersController(sampleOffers())
        val viewModel = OffersViewModel(fakeController)
        advanceUntilIdle()

        // Act
        viewModel.onSearchQueryChange("DataCorp")

        // Assert
        val state = viewModel.uiState.value
        assertEquals(1, state.filteredOffers.size)
        assertEquals("Backend Developer", state.filteredOffers.first().title)
    }

    /**
     * Comprova que la cerca filtra per ubicació.
     */
    @Test
    fun onSearchQueryChange_filtraPerUbicacio() = runTest {
        // Arrange
        SessionData.currentUser = com.impulsfp.mobile.data.User(
            username = "ramon",
            role = "ALUMNE",
            sessionId = "session-123"
        )

        val fakeController = SuccessOffersController(sampleOffers())
        val viewModel = OffersViewModel(fakeController)
        advanceUntilIdle()

        // Act
        viewModel.onSearchQueryChange("Girona")

        // Assert
        val state = viewModel.uiState.value
        assertEquals(1, state.filteredOffers.size)
        assertEquals("Frontend Developer", state.filteredOffers.first().title)
    }

    /**
     * Comprova que la cerca filtra per skill requerida.
     */
    @Test
    fun onSearchQueryChange_filtraPerSkill() = runTest {
        // Arrange
        SessionData.currentUser = com.impulsfp.mobile.data.User(
            username = "ramon",
            role = "ALUMNE",
            sessionId = "session-123"
        )

        val fakeController = SuccessOffersController(sampleOffers())
        val viewModel = OffersViewModel(fakeController)
        advanceUntilIdle()

        // Act
        viewModel.onSearchQueryChange("Spring")

        // Assert
        val state = viewModel.uiState.value
        assertEquals(1, state.filteredOffers.size)
        assertEquals("Backend Developer", state.filteredOffers.first().title)
    }

    /**
     * Comprova que si la cerca no coincideix amb res,
     * el resultat filtrat queda buit.
     */
    @Test
    fun onSearchQueryChange_senseResultats_retornaLlistaBuida() = runTest {
        // Arrange
        SessionData.currentUser = com.impulsfp.mobile.data.User(
            username = "ramon",
            role = "ALUMNE",
            sessionId = "session-123"
        )

        val fakeController = SuccessOffersController(sampleOffers())
        val viewModel = OffersViewModel(fakeController)
        advanceUntilIdle()

        // Act
        viewModel.onSearchQueryChange("Photoshop")

        // Assert
        val state = viewModel.uiState.value
        assertTrue(state.filteredOffers.isEmpty())
    }
}