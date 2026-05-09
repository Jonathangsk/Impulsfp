package com.impulsfp.mobile.data

/**
 * Estat de la interfície d'usuari per a la pantalla d'ofertes.
 *
 * Aquesta classe conté tota la informació necessària per gestionar
 * i representar les ofertes disponibles dins la interfície.
 *
 * Inclou la llista completa d'ofertes, les ofertes filtrades,
 * els filtres aplicats per l'usuari, l'estat de càrrega
 * i possibles missatges d'error.
 *
 * @property offers Llista completa d'ofertes disponibles
 * @property filteredOffers Llista d'ofertes després d'aplicar els filtres
 * @property searchQuery Text introduït per l'usuari per cercar ofertes
 * @property selectedCity Ciutat seleccionada com a filtre
 * @property selectedModality Modalitat seleccionada com a filtre
 * @property availableCities Llista de ciutats disponibles per filtrar
 * @property availableModalities Llista de modalitats disponibles per filtrar
 * @property isLoading Indica si s'està realitzant una operació de càrrega
 * @property errorMessage Missatge d'error descriptiu si s'ha produït algun problema
 *
 * @author abenitez
 */
data class OffersUiState(
    val offers: List<Offer> = emptyList(),
    val filteredOffers: List<Offer> = emptyList(),
    val searchQuery: String = "",
    val selectedCity: String = "",
    val selectedModality: String = "",
    val availableCities: List<String> = emptyList(),
    val availableModalities: List<String> = listOf("REMOTE", "HYBRID", "ONSITE"),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)