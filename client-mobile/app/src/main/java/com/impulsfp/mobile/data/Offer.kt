package com.impulsfp.mobile.data

/**
 * Model de dades que representa una oferta laboral dins l'aplicació.
 *
 * Aquesta classe conté tota la informació necessària per mostrar
 * una oferta de feina o pràctiques a la interfície d'usuari.
 *
 * Inclou dades descriptives de l'oferta, informació de l'empresa,
 * requisits, modalitat laboral i possibles proves associades
 * al procés de selecció.
 *
 * @property id Identificador únic de l'oferta
 * @property title Títol de l'oferta
 * @property description Descripció detallada de l'oferta
 * @property company Nom de l'empresa que publica l'oferta
 * @property requiredSkills Llista d'habilitats requerides per a l'oferta
 * @property location Ubicació de l'oferta
 * @property modality Modalitat de treball de l'oferta
 * @property contractType Tipus de contracte ofert
 * @property salary Salari ofert, si està informat
 * @property createdAt Data de creació o publicació de l'oferta
 * @property state Estat actual de l'oferta
 * @property applicantsCount Nombre de candidatures registrades a l'oferta
 * @property cycle Cicle formatiu relacionat amb l'oferta
 * @property testType Tipus de prova associada al procés de selecció
 * @property testQuestion Pregunta o enunciat de la prova associada
 * @property codeSnippet Fragment de codi associat a una possible prova tècnica
 * @property options Llista d'opcions disponibles per a proves tipus test
 *
 * @author abenitez
 */

data class Offer(
    val id: String,
    val title: String,
    val description: String,
    val company: String,
    val requiredSkills: List<String>,
    val location: String,
    val modality: String,
    val contractType: String,
    val salary: String? = null,
    val createdAt: String,
    val state: String,
    val applicantsCount: Int = 0,
    val cycle: String = "",
    val testType: String? = null,
    val testQuestion: String? = null,
    val codeSnippet: String? = null,
    val options: List<String> = emptyList()
)