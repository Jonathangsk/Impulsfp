package com.impulsfp.mobile.ui

import com.impulsfp.mobile.data.SessionData
import com.impulsfp.mobile.data.User
import org.junit.After
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull

class MenuViewModelTest {

    @After
    fun tearDown() {
        SessionData.logout()
    }

    @Test
    fun logout_clean_session() {
        SessionData.currentUser = User(
            id = 2,
            username = "empresa",
            role = "EMPRESA"
        )

        val viewModel = MenuViewModel()
        viewModel.logout()

        assertNull(SessionData.currentUser)
        assertFalse(SessionData.isLoggedIn())
    }
}