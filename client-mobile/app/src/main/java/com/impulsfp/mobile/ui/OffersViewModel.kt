package com.impulsfp.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impulsfp.mobile.data.Offer
import com.impulsfp.mobile.data.OffersUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OffersViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OffersUiState())
    val uiState: StateFlow<OffersUiState> = _uiState.asStateFlow()

    init {
        loadOffers()
    }

    private fun loadOffers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            //Simulació càrrega del servidor
            delay(1000)

            val mockOffers = getMockOffers()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                offers = mockOffers,
                filteredOffers = mockOffers
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        val currentState = _uiState.value

        val filtered = currentState.offers.filter { offer ->
            offer.title.contains(query, ignoreCase = true) ||
                    offer.company.contains(query, ignoreCase = true) ||
                    offer.location.contains(query, ignoreCase = true) ||
                    offer.requiredSkills.any { it.contains(query, ignoreCase = true)}
        }

        _uiState.value = currentState.copy(
            searchQuery = query,
            filteredOffers = filtered

        )
    }

    private fun getMockOffers(): List<Offer> {
        return listOf(
            Offer(
                id = "1",
                title = "Desenvolupador Android",
                description = "Busquem estudiant per desenvolupament d'apps Android amb Kotlin.",
                company = "Tech Solutions",
                requiredSkills = listOf("Kotlin","Android", "Jetpack"),
                location = "Barcelona",
                modality = "Híbrid",
                contractType = "Pràctiques",
                salary = null,
                createdAt = "2026-03-31",
                applicantsCount = 5
            ),
            Offer(
                id = "2",
                title = "Backend Developer",
                description = "Treball amb APIs REST i bases de dades.",
                company = "DataCorp",
                requiredSkills = listOf("Java", "Spring", "SQL"),
                location = "Remot",
                modality = "Remot",
                contractType = "Pràctiques",
                salary = null,
                createdAt = "2026-03-05",
                applicantsCount = 3
            ),
            Offer(
                id = "3",
                title = "Frontend Developer",
                description = "Desenvolupament amb React.",
                company = "Webify",
                requiredSkills = listOf("React", "JavaScript", "CSS"),
                location = "Girona",
                modality = "Presencial",
                contractType = "Pràctiques",
                salary = null,
                createdAt = "2026-03-10",
                applicantsCount = 2
            )
        )
    }

}