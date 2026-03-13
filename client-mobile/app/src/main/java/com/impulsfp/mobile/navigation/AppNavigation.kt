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
                }
            )
        }

        composable(AppScreen.Menu.route) {
            MenuScreen(
                onLogout = {
                    navController.navigate(AppScreen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}