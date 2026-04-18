package com.impulsfp.mobile.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import com.impulsfp.mobile.communications.ProfileController
import com.impulsfp.mobile.data.SessionData
import com.impulsfp.mobile.data.UserProfile
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditProfileScreenTest {

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
                    preferredLocation = "Matins",
                    availability = "Immediata",
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
    fun editProfileScreen_mostraFormulariBasic() {
        val fakeController = SuccessProfileController()
        val profileViewModel = ProfileViewModel(fakeController)

        profileViewModel.refreshProfile("fake-session")

        composeTestRule.setContent {
            EditProfileScreen(
                onHomeClick = {},
                onSaveSuccess = {},
                onProfileClick = {},
                onApplicationsClick = {},
                onLogout = {},
                profileViewModel = profileViewModel
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Editar perfil").assertIsDisplayed()

        composeTestRule.onNodeWithText("Guardar").performScrollTo()
        composeTestRule.onNodeWithText("Guardar").assertIsDisplayed()

        composeTestRule.onNodeWithText("Cancel·lar").performScrollTo()
        composeTestRule.onNodeWithText("Cancel·lar").assertIsDisplayed()

        composeTestRule.onNodeWithText("Marc").assertExists()
        composeTestRule.onNodeWithText("Garcia").assertExists()
        composeTestRule.onNodeWithText("DAM").assertExists()
    }
}