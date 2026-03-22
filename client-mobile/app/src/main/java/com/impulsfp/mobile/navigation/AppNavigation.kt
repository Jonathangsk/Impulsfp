package com.impulsfp.mobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.impulsfp.mobile.ui.LoginScreen
import com.impulsfp.mobile.ui.MenuScreen

/**
 * Defineix les rutes de navegació de l'aplicació.
 *
 * Cada objecte representa una pantalla accessible dins del sistema de navegació.
 *
 * @property route Ruta associada a cada pantalla
 *
 * @author abenitez
 */

sealed class AppScreen(val route: String) {
    object Login : AppScreen("login")
    object Menu : AppScreen("menu")
}

/**
 * Gestiona la navegació principal de l'aplicació utilitzant Navigation Compose.
 *
 * Defineix el flux entre pantalles:
 * - Login -> Menu d'usuari (quan el login és correcte)
 * - Menu d'usuari -> Login (quan es fa el logout)
 *
 * Permet injectar implementacions personalitzades de les pantalles,
 * facilitant la realitzacií de proves.
 *
 * @param loginScreen Composable de la pantalla de login
 * @param menuScreen Composable de la pantalla de menú d'usuari
 *
 * @author abenitez
 */
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