package com.impulsfp.mobile.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.impulsfp.mobile.ui.ApplicationsScreen
import com.impulsfp.mobile.ui.EditProfileScreen
import com.impulsfp.mobile.ui.LoginScreen
import com.impulsfp.mobile.ui.LoginViewModel
import com.impulsfp.mobile.ui.OfferDetailScreen
import com.impulsfp.mobile.ui.OffersScreen
import com.impulsfp.mobile.ui.OffersViewModel
import com.impulsfp.mobile.ui.ProfileScreen
import com.impulsfp.mobile.ui.ProfileViewModel
import com.impulsfp.mobile.ui.RegisterScreen

sealed class AppScreen(val route: String) {
    object Login : AppScreen("login")
    object Register : AppScreen("register")
    object Offers : AppScreen("offers")
    object Profile : AppScreen("profile")
    object EditProfile : AppScreen("edit_profile")
    object Applications : AppScreen("applications")
    object OfferDetail : AppScreen("offer_detail/{offerId}") {
        fun createRoute(offerId: String) = "offer_detail/$offerId"
    }
}

@Composable
fun AppNavigation(
    loginViewModel: LoginViewModel? = null
) {
    val navController = rememberNavController()
    val profileViewModel: ProfileViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AppScreen.Login.route
    ) {
        composable(AppScreen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppScreen.Offers.route) {
                        popUpTo(AppScreen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(AppScreen.Register.route)
                },
                loginViewModel = loginViewModel ?: viewModel()
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

        composable(AppScreen.Offers.route) {
            val offersViewModel: OffersViewModel = viewModel()

            OffersScreen(
                onLogout = {
                    navController.navigate(AppScreen.Login.route) {
                        popUpTo(0)
                    }
                },
                onProfileClick = {
                    navController.navigate(AppScreen.Profile.route)
                },
                onApplicationsClick = {
                    navController.navigate(AppScreen.Applications.route)
                },
                onOfferClick = { offerId ->
                    navController.navigate(AppScreen.OfferDetail.createRoute(offerId))
                },
                offersViewModel = offersViewModel
            )
        }

        composable(
            route = AppScreen.OfferDetail.route,
            arguments = listOf(
                navArgument("offerId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val offersViewModel: OffersViewModel = viewModel()

            val offerId = backStackEntry.arguments?.getString("offerId")
            val offersUiState by offersViewModel.uiState.collectAsState()
            val offer = offersUiState.offers.find { it.id == offerId }

            val profile = profileViewModel.profile

            if (offer != null) {
                OfferDetailScreen(
                    offer = offer,
                    userName = profile.name,
                    onHomeClick = {
                        navController.navigate(AppScreen.Offers.route) {
                            popUpTo(AppScreen.Offers.route) { inclusive = false }
                        }
                    },
                    onApplicationsClick = {
                        navController.navigate(AppScreen.Applications.route)
                    },
                    onProfileClick = {
                        navController.navigate(AppScreen.Profile.route)
                    },
                    onLogoutClick = {
                        navController.navigate(AppScreen.Login.route) {
                            popUpTo(0)
                        }
                    },
                    onBackClick = {
                        navController.popBackStack()
                    },
                    offersViewModel = offersViewModel
                )
            }
        }

        composable(AppScreen.Profile.route) {
            ProfileScreen(
                onHomeClick = {
                    navController.navigate(AppScreen.Offers.route) {
                        popUpTo(AppScreen.Offers.route) { inclusive = false }
                    }
                },
                onEditProfile = {
                    navController.navigate(AppScreen.EditProfile.route)
                },
                onApplicationsClick = {
                    navController.navigate(AppScreen.Applications.route)
                },
                onLogout = {
                    navController.navigate(AppScreen.Login.route) {
                        popUpTo(0)
                    }
                },
                profileViewModel = profileViewModel
            )
        }

        composable(AppScreen.EditProfile.route) {
            EditProfileScreen(
                onHomeClick = {
                    navController.navigate(AppScreen.Offers.route) {
                        popUpTo(AppScreen.Offers.route) { inclusive = false }
                    }
                },
                onSaveSuccess = {
                    navController.popBackStack()
                },
                onProfileClick = {
                    navController.popBackStack()
                },
                onApplicationsClick = {
                    navController.navigate(AppScreen.Applications.route)
                },
                onLogout = {
                    navController.navigate(AppScreen.Login.route) {
                        popUpTo(0)
                    }
                },
                profileViewModel = profileViewModel
            )
        }

        composable(AppScreen.Applications.route) {
            ApplicationsScreen(
                onHomeClick = {
                    navController.navigate(AppScreen.Offers.route) {
                        popUpTo(AppScreen.Offers.route) { inclusive = false }
                    }
                },
                onProfileClick = {
                    navController.navigate(AppScreen.Profile.route)
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