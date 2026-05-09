package com.impulsfp.mobile.data

/**
 * Model de dades que representa una candidatura dins la interfície d'usuari.
 *
 * Aquesta classe conté la informació necessària per mostrar
 * una candidatura enviada per l'usuari, incloent-hi les dades
 * principals de l'oferta i l'estat actual del procés.
 *
 * @property id Identificador únic de la candidatura
 * @property offerTitle Títol de l'oferta associada a la candidatura
 * @property companyName Nom de l'empresa que publica l'oferta
 * @property location Ubicació de l'oferta
 * @property status Estat actual de la candidatura
 * @property appliedAt Data en què s'ha enviat la candidatura
 *
 * @author abenitez
 */
data class ApplicationUiModel(
    val id: Int,
    val offerTitle: String,
    val companyName: String,
    val location: String,
    val status: String,
    val appliedAt: String
)