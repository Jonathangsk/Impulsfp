package com.impulsfp.mobile.ui

import com.impulsfp.mobile.MainDispatcherRule
import com.impulsfp.mobile.communications.ApplicationsController
import com.impulsfp.mobile.communications.OffersController
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

class OffersViewModelTechnicalTestTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    class FakeOffersController : OffersController() {
        override suspend fun getOffers(sessionId: String) =
            Result.success(emptyList<com.impulsfp.mobile.data.Offer>())
    }

    class SuccessApplicationsController : ApplicationsController() {
        var applyCalled = false
        var receivedOfferId: String? = null
        var receivedSessionId: String? = null
        var receivedAnswer: String? = null

        override suspend fun apply(
            offerId: String,
            sessionId: String,
            answer: String?
        ): Result<String> {
            applyCalled = true
            receivedOfferId = offerId
            receivedSessionId = sessionId
            receivedAnswer = answer
            return Result.success("Aplicació enviada correctament")
        }
    }

    @Test
    fun technicalTest_inicialmentNoEstaCompletat() = runTest {
        SessionData.currentUser = User(
            username = "alumne1",
            role = "ALUMNE",
            sessionId = "session-1"
        )

        val viewModel = OffersViewModel(
            offersController = FakeOffersController(),
            applicationsController = SuccessApplicationsController()
        )

        advanceUntilIdle()

        assertFalse(viewModel.isTechnicalTestCompleted("6"))
        assertEquals(null, viewModel.getTechnicalTestAnswer("6"))
    }

    @Test
    fun markTechnicalTestAsCompleted_guardaResposta_iMarcaComCompletat() = runTest {
        SessionData.currentUser = User(
            username = "alumne1",
            role = "ALUMNE",
            sessionId = "session-1"
        )

        val viewModel = OffersViewModel(
            offersController = FakeOffersController(),
            applicationsController = SuccessApplicationsController()
        )

        advanceUntilIdle()

        viewModel.markTechnicalTestAsCompleted(
            offerId = "6",
            answer = "5"
        )

        assertTrue(viewModel.isTechnicalTestCompleted("6"))
        assertEquals("5", viewModel.getTechnicalTestAnswer("6"))
    }

    @Test
    fun markTechnicalTestAsCompleted_ambTimeout_guardaTimeout() = runTest {
        SessionData.currentUser = User(
            username = "alumne1",
            role = "ALUMNE",
            sessionId = "session-1"
        )

        val viewModel = OffersViewModel(
            offersController = FakeOffersController(),
            applicationsController = SuccessApplicationsController()
        )

        advanceUntilIdle()

        viewModel.markTechnicalTestAsCompleted(
            offerId = "6",
            answer = "TIMEOUT"
        )

        assertTrue(viewModel.isTechnicalTestCompleted("6"))
        assertEquals("TIMEOUT", viewModel.getTechnicalTestAnswer("6"))
    }

    @Test
    fun applyToOffer_ambRespostaTecnica_enviaRespostaAlController() = runTest {
        SessionData.currentUser = User(
            username = "alumne1",
            role = "ALUMNE",
            sessionId = "session-1"
        )

        val fakeApplicationsController = SuccessApplicationsController()

        val viewModel = OffersViewModel(
            offersController = FakeOffersController(),
            applicationsController = fakeApplicationsController
        )

        advanceUntilIdle()

        viewModel.markTechnicalTestAsCompleted(
            offerId = "6",
            answer = "5"
        )

        viewModel.applyToOffer("6")
        advanceUntilIdle()

        assertTrue(fakeApplicationsController.applyCalled)
        assertEquals("6", fakeApplicationsController.receivedOfferId)
        assertEquals("session-1", fakeApplicationsController.receivedSessionId)
        assertEquals("5", fakeApplicationsController.receivedAnswer)
    }

    @Test
    fun applyToOffer_ambTimeout_enviaTimeoutAlController() = runTest {
        SessionData.currentUser = User(
            username = "alumne1",
            role = "ALUMNE",
            sessionId = "session-1"
        )

        val fakeApplicationsController = SuccessApplicationsController()

        val viewModel = OffersViewModel(
            offersController = FakeOffersController(),
            applicationsController = fakeApplicationsController
        )

        advanceUntilIdle()

        viewModel.markTechnicalTestAsCompleted(
            offerId = "6",
            answer = "TIMEOUT"
        )

        viewModel.applyToOffer("6")
        advanceUntilIdle()

        assertTrue(fakeApplicationsController.applyCalled)
        assertEquals("TIMEOUT", fakeApplicationsController.receivedAnswer)
    }

    @Test
    fun technicalTest_noEsComparteixEntreSessions() = runTest {
        SessionData.currentUser = User(
            username = "alumne1",
            role = "ALUMNE",
            sessionId = "session-1"
        )

        val viewModel = OffersViewModel(
            offersController = FakeOffersController(),
            applicationsController = SuccessApplicationsController()
        )

        advanceUntilIdle()

        viewModel.markTechnicalTestAsCompleted(
            offerId = "6",
            answer = "5"
        )

        assertTrue(viewModel.isTechnicalTestCompleted("6"))

        SessionData.currentUser = User(
            username = "alumne2",
            role = "ALUMNE",
            sessionId = "session-2"
        )

        assertFalse(viewModel.isTechnicalTestCompleted("6"))
        assertEquals(null, viewModel.getTechnicalTestAnswer("6"))
    }

    @After
    fun tearDown() {
        SessionData.currentUser = null
    }
}