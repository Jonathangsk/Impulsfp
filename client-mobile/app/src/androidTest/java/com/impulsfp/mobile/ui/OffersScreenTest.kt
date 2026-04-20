package com.impulsfp.mobile.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.impulsfp.mobile.communications.OffersController
import com.impulsfp.mobile.data.Offer
import com.impulsfp.mobile.data.SessionData
import com.impulsfp.mobile.data.User
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Test d'interfície de la pantalla d'ofertes.
 *
 * Aquest conjunt de proves valida que [OffersScreen]
 * mostra correctament els elements principals de cerca i filtratge,
 * i que els filtres afecten la visualització de les ofertes
 * quan es treballa amb dades simulades.
 *
 * Es tracta d'un test UI instrumentat de Jetpack Compose,
 * executat dins del directori `androidTest`.
 *
 * @author abenitez
 */
class OffersScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    /**
     * Controlador fake que retorna un conjunt fix d'ofertes
     * per poder provar la pantalla sense dependre del backend.
     */
    class FakeOffersController : OffersController() {
        override suspend fun getOffers(sessionId: String): Result<List<Offer>> {
            return Result.success(
                listOf(
                    Offer(
                        id = "1",
                        title = "Android Developer",
                        description = "Oferta Android",
                        company = "Tech BCN",
                        requiredSkills = listOf("Kotlin", "Compose"),
                        location = "Barcelona",
                        modality = "REMOTE",
                        contractType = "Pràctiques",
                        salary = "600€",
                        createdAt = "2025-05-20",
                        state = "OPEN",
                        applicantsCount = 2,
                        cycle = "DAM"
                    ),
                    Offer(
                        id = "2",
                        title = "Frontend Developer",
                        description = "Oferta Frontend",
                        company = "Web Girona",
                        requiredSkills = listOf("HTML", "CSS", "JS"),
                        location = "Girona",
                        modality = "ONSITE",
                        contractType = "Jornada completa",
                        salary = "1200€",
                        createdAt = "2025-05-19",
                        state = "OPEN",
                        applicantsCount = 3,
                        cycle = "DAW"
                    )
                )
            )
        }
    }

    @Before
    fun setup() {
        SessionData.clear()
        SessionData.currentUser = User(
            username = "alumne1",
            role = "ALUMNE",
            sessionId = "fake-session"
        )
    }

    /**
     * Verifica que la pantalla mostra els elements principals
     * de cerca i filtratge.
     */
    @Test
    fun offersScreen_mostraElementsPrincipals() {
        val offersViewModel = OffersViewModel(
            offersController = FakeOffersController()
        )

        composeTestRule.setContent {
            OffersScreen(
                onLogout = {},
                onProfileClick = {},
                onOfferClick = {},
                onApplicationsClick = {},
                offersViewModel = offersViewModel
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("offersSearchField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("offersCityFilterDropdown").assertIsDisplayed()
        composeTestRule.onNodeWithTag("offersModalityFilterDropdown").assertIsDisplayed()
        composeTestRule.onNodeWithText("Netejar filtres").assertIsDisplayed()
    }

    /**
     * Verifica que el filtre de cerca permet reduir
     * les ofertes visibles segons el text introduït.
     */
    @Test
    fun offersScreen_filtraPerTextDeCerca() {
        val offersViewModel = OffersViewModel(
            offersController = FakeOffersController()
        )

        composeTestRule.setContent {
            OffersScreen(
                onLogout = {},
                onProfileClick = {},
                onOfferClick = {},
                onApplicationsClick = {},
                offersViewModel = offersViewModel
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Android Developer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Frontend Developer").assertIsDisplayed()

        composeTestRule.onNodeWithTag("offersSearchField").performTextInput("Android")

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Android Developer").assertIsDisplayed()
    }

    /**
     * Verifica que el filtre de ciutat mostra correctament
     * les opcions disponibles.
     */
    @Test
    fun offersScreen_mostraOpcionsDeFiltreDeCiutat() {
        val offersViewModel = OffersViewModel(
            offersController = FakeOffersController()
        )

        composeTestRule.setContent {
            OffersScreen(
                onLogout = {},
                onProfileClick = {},
                onOfferClick = {},
                onApplicationsClick = {},
                offersViewModel = offersViewModel
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("offersCityFilterDropdown").performClick()
        composeTestRule.onNodeWithText("Barcelona").assertIsDisplayed()
        composeTestRule.onNodeWithText("Girona").assertIsDisplayed()
    }

    /**
     * Verifica que el filtre de modalitat mostra correctament
     * les opcions disponibles.
     */
    @Test
    fun offersScreen_mostraOpcionsDeFiltreDeModalitat() {
        val offersViewModel = OffersViewModel(
            offersController = FakeOffersController()
        )

        composeTestRule.setContent {
            OffersScreen(
                onLogout = {},
                onProfileClick = {},
                onOfferClick = {},
                onApplicationsClick = {},
                offersViewModel = offersViewModel
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("offersModalityFilterDropdown").performClick()

        composeTestRule.onAllNodesWithText("Remot")
            .fetchSemanticsNodes()
            .isNotEmpty()

        composeTestRule.onAllNodesWithText("Presencial")
            .fetchSemanticsNodes()
            .isNotEmpty()
    }
}