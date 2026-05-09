package com.impulsfp.mobile.network

/**
 * Model de dades que representa la resposta del servidor
 * després d'una operació d'inscripció a una oferta.
 *
 * Aquesta classe s'utilitza per deserialitzar el missatge
 * retornat per l'API després de processar correctament
 * la candidatura de l'usuari.
 *
 * @property message Missatge descriptiu retornat pel servidor
 *
 * @author abenitez
 */

data class ApplyOfferResponse(
    val message: String
)