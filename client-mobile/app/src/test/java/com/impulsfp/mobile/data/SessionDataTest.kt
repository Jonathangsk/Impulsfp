package com.impulsfp.mobile.data

import org.junit.Assert.*
import org.junit.After
import org.junit.Test

class SessionDataTest {

    @After
    fun tearDown() {
        SessionData.logout()
    }

    @Test
    fun isLoggedIn_retorns_false_when_no_user() {
        SessionData.logout()
        assertFalse(SessionData.isLoggedIn())
    }

    @Test
    fun isLoggedIn_retorns_true_when_user_exists() {
        SessionData.currentUser = User(
            id = 1,
            username = "admin",
            role = "ADMIN"
        )
        assertTrue(SessionData.isLoggedIn())
    }

    @Test
    fun logout_delete_currentUser() {
        SessionData.currentUser = User(
            id = 1,
            username = "admin",
            role = "ADMIN"
        )

        SessionData.logout()

        assertNull(SessionData.currentUser)
        assertFalse(SessionData.isLoggedIn())
    }


}