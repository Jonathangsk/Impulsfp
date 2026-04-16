package com.impulsfp.mobile.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import com.impulsfp.mobile.communications.ProfileController
import com.impulsfp.mobile.data.SessionData
import com.impulsfp.mobile.data.UserProfile
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    class SuccessProfileController : ProfileController() {
        override suspend fun getProfile(sessionId: String): Result<UserProfile> {
            return Result.success(
                UserProfile(
                    username = "alumne1",
                    name = "Marc",
                    surname = "Garcia",
                    email = "marc@test.com",
                    phoneNumber = "600111222",
                    city = "Barcelona",
                    bio = "Estudiant DAM",
                    cycle = "DAM",
                    skills = listOf("Kotlin", "SQL"),
                    experienceLevel = "Junior",
                    languages = listOf("Català", "Castellà"),
                    preferredRoles = listOf("Android Developer"),
                    preferredLocation = "Barcelona",
                    availability = "Matins",
                    portfolio = "https://portfolio.test"
                )
            )
        }
    }

    @Before
    fun setup() {
        SessionData.clear()
    }

    @Test
    fun profileScreen_mostraDadesBasiquesDelPerfil() {
        val fakeController = SuccessProfileController()
        val viewModel = ProfileViewModel(fakeController)

        viewModel.refreshProfile("fake-session")

        composeTestRule.setContent {
            ProfileScreen(
                onHomeClick = {},
                onEditProfile = {},
                onApplicationsClick = {},
                onLogout = {},
                profileViewModel = viewModel
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Perfil d'usuari").assertIsDisplayed()
        composeTestRule.onNodeWithText("Marc Garcia").assertIsDisplayed()
        composeTestRule.onAllNodesWithText("marc@test.com")[0].assertIsDisplayed()

        composeTestRule.onNodeWithText("Editar perfil").performScrollTo()
        composeTestRule.onNodeWithText("Editar perfil").assertIsDisplayed()
    }
}