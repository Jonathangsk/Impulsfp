package com.impulsfp.mobile.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.action.ViewActions.swipeUp
import com.impulsfp.mobile.data.Offer
import org.junit.Rule
import org.junit.Test

/**
 * Test d'interfície de la pantalla de detall d'oferta.
 *
 * Aquest conjunt de proves valida que [OfferDetailScreen]
 * mostra correctament la informació principal d'una oferta
 * i els botons d'acció associats.
 *
 * Es tracta d'un test UI instrumentat de Jetpack Compose,
 * executat dins del directori `androidTest`.
 *
 * Les proves comproven:
 * - la visualització del detall de l'oferta
 * - la presència del botó d'inscripció
 * - la presència del botó per tornar enrere
 * - la visualització del nombre de candidats
 *
 * @author abenitez
 */
class OfferDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    /**
     * Oferta simulada utilitzada per validar
     * la renderització de la pantalla.
     */
    private val fakeOffer = Offer(
        id = "offer-1",
        title = "Android Developer Junior",
        description = "Desenvolupament d'apps Android amb Kotlin.",
        company = "Tech BCN",
        requiredSkills = listOf("Kotlin", "Jetpack Compose", "SQL"),
        location = "Barcelona",
        modality = "REMOTE",
        contractType = "Pràctiques",
        salary = "600€",
        createdAt = "2025-05-20",
        state = "OPEN",
        applicantsCount = 5,
        cycle = "DAM"
    )

    /**
     * Verifica que la pantalla de detall mostra
     * la informació principal de l'oferta.
     */
    @Test
    fun offerDetailScreen_mostraInformacioPrincipal() {
        val offersViewModel = OffersViewModel()

        composeTestRule.setContent {
            OfferDetailScreen(
                offer = fakeOffer,
                userName = "Marc",
                onHomeClick = {},
                onApplicationsClick = {},
                onProfileClick = {},
                onLogoutClick = {},
                onBackClick = {},
                offersViewModel = offersViewModel
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("offerDetailScreen").assertIsDisplayed()
        composeTestRule.onNodeWithText("Android Developer Junior").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tech BCN").assertIsDisplayed()
        composeTestRule.onNodeWithText("Descripció").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tecnologies requerides").assertIsDisplayed()
    }

    /**
     * Verifica que la pantalla mostra els botons principals d'acció.
     */

    @Test
    fun offerDetailScreen_conteBotonsDAccio() {
        val offersViewModel = OffersViewModel()

        composeTestRule.setContent {
            OfferDetailScreen(
                offer = fakeOffer,
                userName = "Marc",
                onHomeClick = {},
                onApplicationsClick = {},
                onProfileClick = {},
                onLogoutClick = {},
                onBackClick = {},
                offersViewModel = offersViewModel
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("applyOfferButton").assertExists()
        composeTestRule.onNodeWithTag("backFromOfferDetailButton").assertExists()
    }

    /**
     * Verifica que es mostra correctament
     * el nombre actual de candidats de l'oferta.
     */
    @Test
    fun offerDetailScreen_mostraNombreDeCandidats() {
        val offersViewModel = OffersViewModel()

        composeTestRule.setContent {
            OfferDetailScreen(
                offer = fakeOffer,
                userName = "Marc",
                onHomeClick = {},
                onApplicationsClick = {},
                onProfileClick = {},
                onLogoutClick = {},
                onBackClick = {},
                offersViewModel = offersViewModel
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Candidats actuals: 5").assertIsDisplayed()
    }
}