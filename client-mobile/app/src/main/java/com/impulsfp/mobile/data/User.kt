package com.impulsfp.mobile.data

/**
 * Model que representa un usuari autenticat a l'aplicació.
 *
 * Conté la informació bàsica necessària per gestionar la sessió:
 * - username: nom d'usuari
 * - role: rol dins de l'aplicació (ALUMNE, EMPRESA, ADMIN)
 * - sessionId: identificador de sessió retornat pel servidor
 *
 * @author abenitez
 */

data class User(
    val username: String,
    val role: String,
    val sessionId: String
)
