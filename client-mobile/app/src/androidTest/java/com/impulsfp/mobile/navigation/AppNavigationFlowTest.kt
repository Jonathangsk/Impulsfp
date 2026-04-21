package com.impulsfp.mobile.navigation

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextInput
import com.impulsfp.mobile.communications.AuthController
import com.impulsfp.mobile.data.SessionData
import com.impulsfp.mobile.data.User
import com.impulsfp.mobile.ui.LoginViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests funcionals de navegació de l'aplicació.
 *
 * Aquest conjunt de proves valida el flux real de navegació
 * entre les pantalles principals de l'app a partir del login.
 *
 * S'utilitza un controlador fake d'autenticació per assegurar
 * que el login sempre és correcte i aïllar la navegació de la
 * dependència del backend real.
 */
class AppNavigationFlowTest {

    @get:Rule
    val composeRule = createComposeRule()

    /**
     * Controlador fake per simular un login correcte.
     */
    private class FakeAuthOk : AuthController() {
        override suspend fun login(u: String, p: String): Result<User> {
            return Result.success(User(u, "ALUMNE", "session-123"))
        }
    }

    @Before
    fun setup() {
        SessionData.clear()
    }

    /**
     * Inicialitza l'app fent servir el LoginViewModel fake.
     */
    private fun launchApp() {
        val fakeLoginViewModel = LoginViewModel(FakeAuthOk())

        composeRule.setContent {
            AppNavigation(
                loginViewModel = fakeLoginViewModel
            )
        }
    }

    /**
     * Realitza el flux de login i espera fins arribar a la pantalla d'ofertes.
     */
    private fun loginAndGoToOffers() {
        composeRule.onNodeWithTag("usernameField").performTextInput("test_user")
        composeRule.onNodeWithTag("passwordField").performTextInput("password123")
        composeRule.onNodeWithTag("loginButton").performClick()

        waitUntilTagExists("offersSearchField")
        composeRule.onNodeWithTag("offersSearchField").assertIsDisplayed()
    }

    /**
     * Espera fins que existeixi almenys un node amb el testTag indicat.
     */
    private fun waitUntilTagExists(tag: String, timeout: Long = 5000) {
        composeRule.waitUntil(timeout) {
            composeRule.onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
        }
    }

    /**
     * Matcher per trobar targetes d'oferta amb testTag dinàmic:
     * offerCard_<id>
     */
    private fun hasOfferCardPrefix(): SemanticsMatcher {
        return SemanticsMatcher("TestTag starts with offerCard_") { node ->
            node.config.getOrNull(SemanticsProperties.TestTag)
                ?.startsWith("offerCard_") == true
        }
    }

    /**
     * Verifica el flux mínim:
     * Login -> Offers
     */
    @Test
    fun login_to_offers_navigation_works() {
        launchApp()
        loginAndGoToOffers()

        composeRule.onNodeWithTag("offersSearchField").assertIsDisplayed()
    }

    /**
     * Verifica la navegació:
     * Login -> Offers -> Profile
     *
     * Aquest test assumeix que ProfileScreen té el testTag:
     * "profileScreen"
     */
    @Test
    fun login_to_profile_navigation_works() {
        launchApp()
        loginAndGoToOffers()

        composeRule.onNodeWithTag("profileButton").performClick()

        waitUntilTagExists("profileScreen")
        composeRule.onNodeWithTag("profileScreen").assertIsDisplayed()
    }

    /**
     * Verifica la navegació:
     * Login -> Offers -> Applications
     *
     * Aquest test assumeix que ApplicationsScreen té el testTag:
     * "applicationsScreen"
     */
    @Test
    fun login_to_applications_navigation_works() {
        launchApp()
        loginAndGoToOffers()

        composeRule.onNodeWithTag("applicationsButton").performClick()

        waitUntilTagExists("applicationsScreen")
        composeRule.onNodeWithTag("applicationsScreen").assertIsDisplayed()
    }

    /**
     * Verifica la navegació:
     * Login -> Offers -> Logout -> Login
     */
    @Test
    fun logout_from_offers_returns_to_login() {
        launchApp()
        loginAndGoToOffers()

        composeRule.onNodeWithTag("logoutButton").performClick()

        waitUntilTagExists("loginButton")
        composeRule.onNodeWithTag("loginButton").assertIsDisplayed()
    }

    /**
     * Verifica la navegació:
     * Login -> Offers -> Profile -> EditProfile
     *
     * Aquest test assumeix:
     * - ProfileScreen té testTag "profileScreen"
     * - EditProfileScreen té testTag "editProfileScreen"
     * - el botó d'editar perfil té testTag "editProfileButton"
     */
    @Test
    fun profile_to_edit_profile_navigation_works() {
        launchApp()
        loginAndGoToOffers()

        composeRule.onNodeWithTag("profileButton").performClick()

        waitUntilTagExists("profileScreen")
        composeRule.onNodeWithTag("profileScreen").assertIsDisplayed()

        composeRule.onNodeWithTag("editProfileButton", useUnmergedTree = true)
            .assertExists()

        composeRule.onNodeWithTag("editProfileButton", useUnmergedTree = true)
            .performSemanticsAction(SemanticsActions.OnClick)

        waitUntilTagExists("editProfileScreen", timeout = 8000)
        composeRule.onNodeWithTag("editProfileScreen").assertIsDisplayed()
    }

    /**
     * Verifica la navegació:
     * Login -> Offers -> Profile -> Home -> Offers
     *
     * Aquest test comprova que es pot tornar a la pantalla principal.
     */
    @Test
    fun profile_to_home_returns_to_offers() {
        launchApp()
        loginAndGoToOffers()

        composeRule.onNodeWithTag("profileButton").performClick()
        waitUntilTagExists("profileScreen")
        composeRule.onNodeWithTag("profileScreen").assertIsDisplayed()

        composeRule.onNodeWithTag("homeButton").performClick()

        waitUntilTagExists("offersSearchField")
        composeRule.onNodeWithTag("offersSearchField").assertIsDisplayed()
    }

    /**
     * Verifica la navegació:
     * Login -> Offers -> Applications -> Home -> Offers
     */
    @Test
    fun applications_to_home_returns_to_offers() {
        launchApp()
        loginAndGoToOffers()

        composeRule.onNodeWithTag("applicationsButton").performClick()
        waitUntilTagExists("applicationsScreen")
        composeRule.onNodeWithTag("applicationsScreen").assertIsDisplayed()

        composeRule.onNodeWithTag("homeButton").performClick()

        waitUntilTagExists("offersSearchField")
        composeRule.onNodeWithTag("offersSearchField").assertIsDisplayed()
    }

    /**
     * Verifica la navegació:
     * Login -> Offers -> Profile -> Applications
     *
     * Aquest test comprova navegació creuada entre pantalles del menú superior.
     */
    @Test
    fun profile_to_applications_navigation_works() {
        launchApp()
        loginAndGoToOffers()

        composeRule.onNodeWithTag("profileButton").performClick()
        waitUntilTagExists("profileScreen")
        composeRule.onNodeWithTag("profileScreen").assertIsDisplayed()

        composeRule.onNodeWithTag("applicationsButton").performClick()
        waitUntilTagExists("applicationsScreen")
        composeRule.onNodeWithTag("applicationsScreen").assertIsDisplayed()
    }

    /**
     * Verifica la navegació:
     * Login -> Offers -> Applications -> Profile
     */
    @Test
    fun applications_to_profile_navigation_works() {
        launchApp()
        loginAndGoToOffers()

        composeRule.onNodeWithTag("applicationsButton").performClick()
        waitUntilTagExists("applicationsScreen")
        composeRule.onNodeWithTag("applicationsScreen").assertIsDisplayed()

        composeRule.onNodeWithTag("profileButton").performClick()
        waitUntilTagExists("profileScreen")
        composeRule.onNodeWithTag("profileScreen").assertIsDisplayed()
    }

    /**
     * Verifica el flux:
     * Login -> Offers -> OfferDetail -> Back -> Offers
     *
     * IMPORTANT:
     * Aquest test només funcionarà si a la pantalla d'ofertes hi ha
     * almenys una oferta carregada i visible.
     *
     * Per detectar una targeta d'oferta es fa servir el prefix:
     * offerCard_<id>
     */
    @Test
    fun offer_detail_navigation_and_back_returns_to_offers() {
        launchApp()
        loginAndGoToOffers()

        composeRule.waitUntil(5000) {
            composeRule.onAllNodes(hasOfferCardPrefix()).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onAllNodes(hasOfferCardPrefix())[0].performClick()

        waitUntilTagExists("offerDetailScreen")
        composeRule.onNodeWithTag("offerDetailScreen").assertIsDisplayed()

        composeRule.onNodeWithTag("backFromOfferDetailButton", useUnmergedTree = true)
            .assertExists()

        composeRule.onNodeWithTag("backFromOfferDetailButton", useUnmergedTree = true)
            .performSemanticsAction(SemanticsActions.OnClick)

        waitUntilTagExists("offersSearchField", timeout = 8000)
        composeRule.onNodeWithTag("offersSearchField").assertIsDisplayed()
    }

    /**
     * Verifica el flux:
     * Login -> Offers -> OfferDetail -> Profile
     *
     * IMPORTANT:
     * Aquest test també requereix que existeixi almenys una oferta carregada.
     */
    @Test
    fun offer_detail_to_profile_navigation_works() {
        launchApp()
        loginAndGoToOffers()

        composeRule.waitUntil(5000) {
            composeRule.onAllNodes(hasOfferCardPrefix()).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onAllNodes(hasOfferCardPrefix())[0].performClick()

        waitUntilTagExists("offerDetailScreen")
        composeRule.onNodeWithTag("offerDetailScreen").assertIsDisplayed()

        composeRule.onNodeWithTag("profileButton").performClick()

        waitUntilTagExists("profileScreen")
        composeRule.onNodeWithTag("profileScreen").assertIsDisplayed()
    }

    /**
     * Verifica el flux:
     * Login -> Offers -> OfferDetail -> Applications
     *
     * IMPORTANT:
     * Aquest test també requereix que existeixi almenys una oferta carregada.
     */
    @Test
    fun offer_detail_to_applications_navigation_works() {
        launchApp()
        loginAndGoToOffers()

        composeRule.waitUntil(5000) {
            composeRule.onAllNodes(hasOfferCardPrefix()).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onAllNodes(hasOfferCardPrefix())[0].performClick()

        waitUntilTagExists("offerDetailScreen")
        composeRule.onNodeWithTag("offerDetailScreen").assertIsDisplayed()

        composeRule.onNodeWithTag("applicationsButton").performClick()

        waitUntilTagExists("applicationsScreen")
        composeRule.onNodeWithTag("applicationsScreen").assertIsDisplayed()
    }

    /**
     * Verifica el flux:
     * Login -> Offers -> OfferDetail -> Logout -> Login
     *
     * IMPORTANT:
     * Aquest test també requereix que existeixi almenys una oferta carregada.
     */
    @Test
    fun offer_detail_logout_returns_to_login() {
        launchApp()
        loginAndGoToOffers()

        composeRule.waitUntil(5000) {
            composeRule.onAllNodes(hasOfferCardPrefix()).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onAllNodes(hasOfferCardPrefix())[0].performClick()

        waitUntilTagExists("offerDetailScreen")
        composeRule.onNodeWithTag("offerDetailScreen").assertIsDisplayed()

        composeRule.onNodeWithTag("logoutButton").performClick()

        waitUntilTagExists("loginButton")
        composeRule.onNodeWithTag("loginButton").assertIsDisplayed()
    }
}