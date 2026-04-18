package com.impulsfp.mobile.data

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