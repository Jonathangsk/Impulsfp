package com.impulsfp.mobile.network

/**
 * Model de dades que representa la resposta d'una candidatura
 * retornada pel servidor.
 *
 * Aquesta classe s'utilitza per deserialitzar la informació
 * de les candidatures rebuda des de l'API.
 *
 * Conté les dades principals de l'oferta associada i
 * l'estat actual de la candidatura enviada per l'usuari.
 *
 * @property id Identificador únic de la candidatura
 * @property offerTitle Títol de l'oferta associada
 * @property companyName Nom de l'empresa que publica l'oferta
 * @property location Ubicació de l'oferta
 * @property status Estat actual de la candidatura
 * @property appliedAt Data d'enviament de la candidatura
 *
 * @author abenitez
 */
data class ApplicationResponse(
    val id: Int,
    val offerTitle: String,
    val companyName: String,
    val location: String,
    val status: String,
    val appliedAt: String
)