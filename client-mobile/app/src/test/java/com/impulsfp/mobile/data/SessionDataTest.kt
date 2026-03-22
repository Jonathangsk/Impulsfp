package com.impulsfp.mobile.data

import org.junit.Assert.*
import org.junit.After
import org.junit.Test

/**
 * Test unitaris per verificar el comportament de SessionData.
 * Comprova:
 * - si l'apliació detecta correctament quan hi ha una sessió activa
 * - si el logout elimina correctament l'usuari actual
 * - si es pot recuperar el sessionId de la sessió activa
 *
 *  @author abenitez
 */
class SessionDataTest {

    private fun createTestUser() = User(
        username = "admin",
        role = "ADMIN",
        sessionId = "session-123"
    )
    @After

    fun tearDown() {
        SessionData.logout()
    }

    @Test
    fun isLoggedIn_returns_false_when_no_user() {
        assertFalse(SessionData.isLoggedIn())
    }

    @Test
    fun isLoggedIn_returns_true_when_user_exists() {
        SessionData.currentUser = createTestUser()
        assertTrue(SessionData.isLoggedIn())
    }

    @Test
    fun logout_deletes_currentUser() {
        SessionData.currentUser = createTestUser()

        SessionData.logout()

        assertNull(SessionData.currentUser)
        assertFalse(SessionData.isLoggedIn())
    }

    @Test
    fun getSessionId_returns_sessionId_when_user_exists() {
        SessionData.currentUser = createTestUser()
        assertEquals("session-123", SessionData.getSessionId())
    }

    @Test
    fun getSessionId_returns_null_when_no_user_exists() {
        assertNull(SessionData.getSessionId())
    }


}