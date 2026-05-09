package com.impulsfp.mobile.network

/**
 * Model de dades que representa la resposta d'una oferta
 * retornada pel servidor.
 *
 * Aquesta classe s'utilitza per deserialitzar la informació
 * de les ofertes rebuda des de l'API.
 *
 * Inclou dades descriptives de l'oferta, informació de l'empresa,
 * requisits, modalitat laboral i possibles proves associades
 * al procés de selecció.
 *
 * @property id Identificador únic de l'oferta
 * @property title Títol de l'oferta
 * @property description Descripció detallada de l'oferta
 * @property companyName Nom de l'empresa que publica l'oferta
 * @property location Ubicació de l'oferta
 * @property modality Modalitat de treball de l'oferta
 * @property contractType Tipus de contracte ofert
 * @property salary Salari ofert, si està informat
 * @property skills Llista d'habilitats requerides per a l'oferta
 * @property state Estat actual de l'oferta
 * @property creationDate Data de creació o publicació de l'oferta
 * @property applicantsCount Nombre de candidatures registrades a l'oferta
 * @property cycle Cicle formatiu relacionat amb l'oferta
 * @property testType Tipus de prova associada al procés de selecció
 * @property testQuestion Pregunta o enunciat de la prova associada
 * @property codeSnippet Fragment de codi associat a una possible prova tècnica
 * @property options Opcions disponibles per a proves tipus test
 *
 * @author abenitez
 */
data class OfferResponse(
    val id: Int,
    val title: String,
    val description: String,
    val companyName: String,
    val location: String,
    val modality: String,
    val contractType: String,
    val salary: Double?,
    val skills: List<String>,
    val state: String,
    val creationDate: String,
    val applicantsCount: Int?,
    val cycle: String?,
    val testType: String?,
    val testQuestion: String?,
    val codeSnippet: String?,
    val options: String?
)
