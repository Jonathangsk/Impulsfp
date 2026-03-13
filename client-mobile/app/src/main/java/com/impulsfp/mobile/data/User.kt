package com.impulsfp.mobile.data

/**
 * Model que representa un usuari autenticat a l'aplicació.
 * Conté la informació bàsica de l'usuari: identificador únic a la BD,
 * nom d'usuari i rol que determina els permisos dins de l'aplicació (user, admin)
 */

data class User(
    val id: Int,
    val username: String,
    val role: String
)
