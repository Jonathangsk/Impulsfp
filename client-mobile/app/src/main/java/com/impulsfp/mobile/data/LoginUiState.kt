package com.impulsfp.mobile.data

/**
 * Representa l'estat de la pantalla de login.
 * Guarda les dades introduïdes per l'usuari (username i password),
 * així com l'estat del procés d'autenticació (carregant, error o èxit).
 */

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false
)