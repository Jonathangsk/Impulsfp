package com.impulsfp.mobile.network

/**
 * Model de dades utilitzat per enviar una candidatura
 * a una oferta laboral.
 *
 * Aquesta classe conté la informació necessària perquè
 * el servidor pugui registrar la candidatura de l'usuari,
 * incloent-hi la possible resposta a una prova tècnica.
 *
 * @property offerId Identificador de l'oferta a la qual es vol aplicar
 * @property answer Resposta opcional associada a la prova tècnica de l'oferta
 *
 * @author abenitez
 */
data class ApplyRequest(
    val offerId: String,
    val answer: String? // null si no hi ha test
)