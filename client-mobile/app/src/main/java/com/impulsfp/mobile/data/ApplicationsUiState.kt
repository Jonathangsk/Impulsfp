package com.impulsfp.mobile.data

/**
 * Estat de la interfície d'usuari per a la pantalla de candidatures.
 *
 * Aquesta classe conté tota la informació necessària per representar
 * l'estat actual de les candidatures de l'usuari dins la interfície.
 *
 * Inclou la llista de candidatures disponibles, l'estat de càrrega
 * i possibles missatges d'error produïts durant les operacions.
 *
 * @property applications Llista de candidatures de l'usuari
 * @property isLoading Indica si s'està realitzant una operació de càrrega
 * @property errorMessage Missatge d'error descriptiu si s'ha produït algun problema
 *
 * @author abenitez
 */
data class ApplicationsUiState(
    val applications: List<ApplicationUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)