package com.impulsfp.mobile.network

/**
 * Model de resposta retornat pel servidor després
 * d'una actualització correcta del perfil d'usuari.
 *
 * Aquesta classe conté el missatge informatiu enviat
 * pel backend per confirmar que l'operació s'ha completat
 * satisfactòriament.
 *
 * Habitualment s'utilitza per mostrar una notificació
 * o confirmació a la interfície de l'aplicació.
 *
 * En tractar-se d'una `data class`, Kotlin genera automàticament
 * mètodes útils com `copy()`, `equals()`, `hashCode()` i `toString()`.
 *
 * @property message Missatge de confirmació retornat pel servidor
 *
 * @author abenitez
 */
data class UpdateProfileResponse(
    val message: String
)