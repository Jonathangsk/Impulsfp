package com.impulsfp.mobile.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

/**
 * Test d'interfície de la pantalla de candidatures.
 *
 * Aquest conjunt de proves valida que [ApplicationsScreen]
 * es renderitza correctament i mostra la seva estructura bàsica.
 *
 * Es tracta d'un test UI instrumentat de Jetpack Compose,
 * executat dins del directori `androidTest`.
 *
 * Aquesta prova assumeix que la pantalla incorpora
 * el testTag "applicationsScreen".
 *
 * @author abenitez
 */
class ApplicationsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    /**
     * Verifica que la pantalla de candidatures es mostra correctament.
     */
    @Test
    fun applicationsScreen_esMostraCorrectament() {
        composeTestRule.setContent {
            ApplicationsScreen(
                onHomeClick = {},
                onProfileClick = {},
                onLogout = {}
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("applicationsScreen").assertIsDisplayed()
    }

    /**
     * Verifica que la barra superior comuna és present
     * a la pantalla de candidatures.
     */
    @Test
    fun applicationsScreen_mostraNavegacioSuperior() {
        composeTestRule.setContent {
            ApplicationsScreen(
                onHomeClick = {},
                onProfileClick = {},
                onLogout = {}
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("homeButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("profileButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("logoutButton").assertIsDisplayed()
    }
}