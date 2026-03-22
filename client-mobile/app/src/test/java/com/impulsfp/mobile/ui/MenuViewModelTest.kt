package com.impulsfp.mobile.ui

import com.impulsfp.mobile.communications.AuthController
import com.impulsfp.mobile.data.SessionData
import com.impulsfp.mobile.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue


/**
 * Tests unitaris del ViewModel del menú.
 * Verifiquen que el procés de logout amb dades simulades (fakes).
 * - neteja la sessió local
 * - executa l'acció final indicada
 *
 *  @author abenitez
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MenuViewModelTest {

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

    private class FakeAuthControllerSuccess : AuthController() {
        override suspend fun logout(sessionId: String): Result<Unit> {
            return Result.success(Unit)
        }
    }
    @Test
    fun logout_clean_session_and_calls_onFinished() = runTest {
        SessionData.currentUser = User(
            username = "empresa",
            role = "EMPRESA",
            sessionId = "session-456"
        )

        val viewModel = MenuViewModel(FakeAuthControllerSuccess())
        var onFinishedCalled = false

        viewModel.logout {
            onFinishedCalled = true
        }

        advanceUntilIdle()

        assertNull(SessionData.currentUser)
        assertFalse(SessionData.isLoggedIn())
        assertTrue(onFinishedCalled)
    }
}