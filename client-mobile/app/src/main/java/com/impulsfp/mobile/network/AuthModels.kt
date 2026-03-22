package com.impulsfp.mobile.network

/**
 * Models de dades utilitzats per a la configuració amb el servidor
 * en les operacions d'autenticació (login i logout).
 *
 * Inclou les estructures de request i response per als endpoints
 * definits a l'API REST
 *
 * @author abenitez
 */

/**
 * Petició de login enviada al servidor.
 *
 * @property username Nom d'usuari introduït per l'usuari
 * @property password Contrasenya introduïda per l'usuari
 */
data class LoginRequest(
    val username: String,
    val password: String
)

/**
 * Resposta correcta del servidor després d'un login.
 *
 * @property sessionId Identificador de sessió generat per servidor.
 * @property userType Tipus d'usuari retornat pel backend (student, company, admin)
 */
data class LoginResponse(
    val sessionId: String,
    val userType: String
)

/**
 * Resposta d'error retornada pel servidor.
 *
 * @property error Missatge d'error descriptiu
 */
data class ErrorResponse(
    val error: String
)

/**
 * Petició de logout enviada al servidor.
 *
 * @property sessionId Identificador de sessió de l'usuari autenticat
 */
data class LogoutRequest(
    val sessionId: String
)

/**
 * Resposta correcta del servidor després d'un logout.
 *
 * @property message Missatge de confirmació del servidor.
 */
data class LogoutResponse(
    val message: String
)