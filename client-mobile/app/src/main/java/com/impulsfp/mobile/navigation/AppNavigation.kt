package com.impulsfp.mobile.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.impulsfp.mobile.ui.EditProfileScreen
import com.impulsfp.mobile.ui.LoginScreen
import com.impulsfp.mobile.ui.MenuScreen
import com.impulsfp.mobile.ui.ProfileScreen
import com.impulsfp.mobile.ui.RegisterScreen
import com.impulsfp.mobile.ui.OfferDetailScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.impulsfp.mobile.ui.OffersViewModel

/**
 * Defineix les rutes de navegació de l'aplicació.
 *
 * @property route Ruta associada a cada pantalla.
 *
 * @author abenitez
 */
sealed class AppScreen(val route: String) {
    object Login : AppScreen("login")
    object Register : AppScreen("register")
    object Menu : AppScreen("menu")
    object Profile : AppScreen("profile")
    object EditProfile : AppScreen("edit_profile")
    object OfferDetail : AppScreen("offer_detail/{offerId}") {
        fun createRoute(offerId: String) = "offer_detail/$offerId"
    }
}

/**
 * Gestiona la navegació principal de l'aplicació.
 *
 * @param loginScreen Pantalla de login.
 * @param menuScreen Pantalla de menú.
 * @param profileScreen Pantalla de perfil.
 * @param editProfileScreen Pantalla d'edició del perfil.
 * @param registerScreen Pantalla de registre d'usuari.
 *
 * @author abenitez
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreen.Login.route
    ) {
        composable(AppScreen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppScreen.Menu.route) {
                        popUpTo(AppScreen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(AppScreen.Register.route)
                }
            )
        }

        composable(AppScreen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(AppScreen.Login.route) {
                        popUpTo(AppScreen.Register.route) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppScreen.Menu.route) {
            MenuScreen(
                onLogout = {
                    navController.navigate(AppScreen.Login.route) {
                        popUpTo(0)
                    }
                },
                onProfileClick = {
                    navController.navigate(AppScreen.Profile.route)
                },
                onOfferClick = { offerId ->
                    navController.navigate(AppScreen.OfferDetail.createRoute(offerId))
                }
            )
        }

        composable(
            route = AppScreen.OfferDetail.route,
            arguments = listOf(
                navArgument("offerId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val offerId = backStackEntry.arguments?.getString("offerId")
            val offersViewModel: OffersViewModel = viewModel()
            val offersUiState by offersViewModel.uiState.collectAsState()
            val offer = offersUiState.offers.find { it.id == offerId }

            val profile = com.impulsfp.mobile.data.ProfileRepository.getProfile()

            if (offer != null) {
                OfferDetailScreen(
                    offer = offer,
                    userName = profile.name,
                    avatarId = profile.avatarId,
                    onHomeClick = {
                        navController.navigate(AppScreen.Menu.route) {
                            popUpTo(AppScreen.Menu.route) { inclusive = false }
                        }
                    },
                    onProfileClick = {
                        navController.navigate(AppScreen.Profile.route)
                    },
                    onLogoutClick = {
                        navController.navigate(AppScreen.Login.route) {
                            popUpTo(0)
                        }
                    },
                    onApplyClick = {
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
        composable(AppScreen.Profile.route) {
            ProfileScreen(
                onHomeClick = {
                    navController.navigate(AppScreen.Menu.route) {
                        popUpTo(AppScreen.Menu.route) { inclusive = false }
                    }
                },
                onEditProfile = {
                    navController.navigate(AppScreen.EditProfile.route)
                },
                onLogout = {
                    navController.navigate(AppScreen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(AppScreen.EditProfile.route) {
            EditProfileScreen(
                onHomeClick = {
                    navController.navigate(AppScreen.Menu.route) {
                        popUpTo(AppScreen.Menu.route) { inclusive = false }
                    }
                },
                onSaveSuccess = {
                    navController.popBackStack()
                },
                onProfileClick = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate(AppScreen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}
