package com.impulsfp.mobile.data

/**
 * Model de dades utilitzat per sol·licitar el canvi de contrasenya
 * d'un usuari autenticat.
 *
 * Aquesta classe conté la contrasenya actual de l'usuari i
 * la nova contrasenya que es vol establir.
 *
 * @property currentPassword Contrasenya actual de l'usuari
 * @property newPassword Nova contrasenya que es vol assignar al compte
 *
 * @author abenitez
 */
data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)