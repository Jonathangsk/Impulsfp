package com.impulsfp.mobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.impulsfp.mobile.ui.LoginScreen
import com.impulsfp.mobile.ui.MenuScreen

sealed class AppScreen(val route: String) {
    object Login : AppScreen("login")
    object Menu : AppScreen("menu")
}

@Composable
fun AppNavigation(
    loginScreen: @Composable ((() -> Unit) -> Unit) = { onLoginSuccess ->
        LoginScreen(onLoginSuccess = onLoginSuccess)
    },
    menuScreen: @Composable ((() -> Unit) -> Unit) = { onLogout ->
        MenuScreen(onLogout = onLogout)
    }
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreen.Login.route
    ) {
        composable(AppScreen.Login.route) {
            loginScreen {
                navController.navigate(AppScreen.Menu.route) {
                    popUpTo(AppScreen.Login.route) { inclusive = true }
                }
            }
        }

        composable(AppScreen.Menu.route) {
            menuScreen {
                navController.navigate(AppScreen.Login.route) {
                    popUpTo(0)
                }
            }
        }
    }
}