package com.impulsfp.mobile.data

/**
 * Representa l'estat de la pantalla de login.
 *
 * Aquesta classe conté:
 * - Les dades introduïdes per l'usuari (username i password),
 * - L'estat del procés d'autenticació (carregant, error o èxit).
 *
 * S'utilitza per gestionar i observar l'estat de la UI del login
 *
 * @author abenitez
 */

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false
)