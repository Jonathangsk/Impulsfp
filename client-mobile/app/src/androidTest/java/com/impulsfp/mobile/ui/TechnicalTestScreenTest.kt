package com.impulsfp.mobile.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.impulsfp.mobile.data.Offer
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Test d'interfície de la pantalla de proves tècniques.
 *
 * Aquest conjunt de proves valida que [TechnicalTestScreen]
 * es renderitza correctament i mostra la informació esperada.
 *
 * També comprova:
 * - visualització del tipus de prova
 * - visualització de l'enunciat
 * - visualització de les opcions
 * - botó de finalització desactivat inicialment
 * - activació del botó després de seleccionar resposta
 * - execució de l'acció de finalització
 *
 * Test UI instrumentat de Jetpack Compose.
 *
 * @author abenitez
 */
class TechnicalTestScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun sampleOffer(): Offer {
        return Offer(
            id = "6",
            title = "Backend Developer",
            description = "Oferta de prova",
            company = "TechNova",
            requiredSkills = listOf("Java", "SQL"),
            location = "Barcelona",
            modality = "HYBRID",
            contractType = "FP_DUAL",
            salary = "32000",
            createdAt = "2026-05-01",
            state = "OPEN",
            applicantsCount = 12,
            cycle = "DAM",
            testType = "JAVA",
            testQuestion = "Quin és el resultat?",
            codeSnippet = "int x = 5; System.out.println(x++);",
            options = listOf("5", "6", "error", "null")
        )
    }

    /**
     * Verifica que la pantalla de prova tècnica es mostra correctament.
     */
    @Test
    fun technicalTestScreen_esMostraCorrectament() {
        val offersViewModel = OffersViewModel()

        composeTestRule.setContent {
            TechnicalTestScreen(
                offer = sampleOffer(),
                offersViewModel = offersViewModel,
                onBackClick = {},
                onTestCompleted = {}
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("technicalTestScreen")
            .assertIsDisplayed()
    }

    /**
     * Verifica que es mostren el tipus de prova,
     * l'enunciat i el fragment de codi.
     */
    @Test
    fun technicalTestScreen_mostraInformacioProva() {
        val offersViewModel = OffersViewModel()

        composeTestRule.setContent {
            TechnicalTestScreen(
                offer = sampleOffer(),
                offersViewModel = offersViewModel,
                onBackClick = {},
                onTestCompleted = {}
            )
        }


        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Tipus: JAVA").assertIsDisplayed()
        composeTestRule.onNodeWithText("Quin és el resultat?").assertIsDisplayed()
        composeTestRule
            .onNodeWithText("int x = 5; System.out.println(x++);")
            .assertIsDisplayed()
    }

    /**
     * Verifica que les opcions de resposta es mostren correctament.
     */
    @Test
    fun technicalTestScreen_mostraOpcionsResposta() {
        val offersViewModel = OffersViewModel()

        composeTestRule.setContent {
            TechnicalTestScreen(
                offer = sampleOffer(),
                offersViewModel = offersViewModel,
                onBackClick = {},
                onTestCompleted = {}
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("5").assertIsDisplayed()
        composeTestRule.onNodeWithText("6").assertIsDisplayed()
        composeTestRule.onNodeWithText("error").assertIsDisplayed()
        composeTestRule.onNodeWithText("null").assertIsDisplayed()
    }

    /**
     * Verifica que el botó de finalitzar està desactivat inicialment.
     */
    @Test
    fun technicalTestScreen_botoFinalitzar_inicialmentDesactivat() {
        val offersViewModel = OffersViewModel()

        composeTestRule.setContent {
            TechnicalTestScreen(
                offer = sampleOffer(),
                offersViewModel = offersViewModel,
                onBackClick = {},
                onTestCompleted = {}
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("Finalitzar prova")
            .assertIsNotEnabled()
    }

    /**
     * Verifica que el botó de finalitzar s'activa
     * després de seleccionar una resposta.
     */
    @Test
    fun technicalTestScreen_botoFinalitzar_sActivaQuanSeleccionaResposta() {
        val offersViewModel = OffersViewModel()

        composeTestRule.setContent {
            TechnicalTestScreen(
                offer = sampleOffer(),
                offersViewModel = offersViewModel,
                onBackClick = {},
                onTestCompleted = {}
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("5").performClick()

        composeTestRule
            .onNodeWithText("Finalitzar prova")
            .assertIsEnabled()
    }

    /**
     * Verifica que en seleccionar una resposta i prémer
     * el botó de finalitzar, s'executa el callback de finalització.
     */
    @Test
    fun technicalTestScreen_finalitzarProva_executaOnTestCompleted() {
        var completedCalled = false
        val offersViewModel = OffersViewModel()

        composeTestRule.setContent {
            TechnicalTestScreen(
                offer = sampleOffer(),
                offersViewModel = offersViewModel,
                onBackClick = {},
                onTestCompleted = {
                    completedCalled = true
                }
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("5").performClick()

        composeTestRule
            .onNodeWithText("Finalitzar prova")
            .performClick()

        composeTestRule.waitForIdle()

        assertTrue(completedCalled)
    }
}