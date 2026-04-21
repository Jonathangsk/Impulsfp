package com.impulsfp.mobile.network

/**
 * Model de resposta retornat pel servidor després
 * d'un registre correcte d'usuari.
 *
 * Aquesta classe conté la informació necessària per iniciar
 * la sessió automàticament després del procés de registre.
 *
 * Habitualment el backend retorna un identificador de sessió
 * i el tipus d'usuari assignat al nou compte.
 *
 * En tractar-se d'una `data class`, Kotlin genera automàticament
 * mètodes útils com `copy()`, `equals()`, `hashCode()` i `toString()`.
 *
 * @property sessionId Identificador de sessió creat després del registre
 * @property userType Tipus d'usuari retornat pel servidor
 *
 * @author abenitez
 */
data class RegisterResponse(
    val sessionId: String,
    val userType: String
)