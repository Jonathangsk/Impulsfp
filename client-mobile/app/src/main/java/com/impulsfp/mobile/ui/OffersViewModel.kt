package com.impulsfp.mobile.ui

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impulsfp.mobile.communications.ApplicationsController
import com.impulsfp.mobile.communications.OffersController
import com.impulsfp.mobile.data.Offer
import com.impulsfp.mobile.data.OffersUiState
import com.impulsfp.mobile.data.SessionData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OffersViewModel(
    private val offersController: OffersController = OffersController(),
    private val applicationsController: ApplicationsController = ApplicationsController()
) : ViewModel() {

    private val _uiState = MutableStateFlow(OffersUiState())
    val uiState: StateFlow<OffersUiState> = _uiState.asStateFlow()

    private val _applyLoading = MutableStateFlow(false)
    val applyLoading: StateFlow<Boolean> = _applyLoading.asStateFlow()

    private val _applySuccessMessage = MutableStateFlow<String?>(null)
    val applySuccessMessage: StateFlow<String?> = _applySuccessMessage.asStateFlow()

    private val _applyErrorMessage = MutableStateFlow<String?>(null)
    val applyErrorMessage: StateFlow<String?> = _applyErrorMessage.asStateFlow()

    private val technicalTestAnswers = mutableStateMapOf<String, String>()

    init {
        loadOffers()
    }

    private fun technicalTestKey(offerId: String): String {
        val sessionId = SessionData.getSessionId() ?: ""
        return "$sessionId-$offerId"
    }

    fun loadOffers() {
        val sessionId = SessionData.getSessionId()

        if (sessionId.isNullOrBlank()) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "No hi ha cap sessió activa"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val result = offersController.getOffers(sessionId)

            result.onSuccess { offers ->
                val cities = offers
                    .map { it.location.trim() }
                    .filter { it.isNotBlank() }
                    .distinct()
                    .sorted()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    offers = offers,
                    availableCities = cities,
                    filteredOffers = filterOffers(
                        offers = offers,
                        query = _uiState.value.searchQuery,
                        city = _uiState.value.selectedCity,
                        modality = _uiState.value.selectedModality
                    ),
                    errorMessage = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "Error en carregar les ofertes"
                )
            }
        }
    }

    fun applyToOffer(offerId: String) {
        val sessionId = SessionData.getSessionId()

        if (sessionId.isNullOrBlank()) {
            _applyErrorMessage.value = "No hi ha cap sessió activa"
            return
        }

        viewModelScope.launch {
            _applyLoading.value = true
            _applySuccessMessage.value = null
            _applyErrorMessage.value = null

            val result = applicationsController.apply(
                offerId = offerId,
                sessionId = sessionId,
                answer = technicalTestAnswers[technicalTestKey(offerId)]
            )

            result.onSuccess { message ->
                _applySuccessMessage.value = message
                _applyLoading.value = false
            }.onFailure { error ->
                _applyErrorMessage.value =
                    error.message ?: "No s'ha pogut completar la inscripció"
                _applyLoading.value = false
            }
        }
    }

    fun markTechnicalTestAsCompleted(
        offerId: String,
        answer: String
    ) {
        technicalTestAnswers[technicalTestKey(offerId)] = answer
    }

    fun isTechnicalTestCompleted(offerId: String): Boolean {
        return technicalTestAnswers.containsKey(technicalTestKey(offerId))
    }

    fun clearApplyMessages() {
        _applySuccessMessage.value = null
        _applyErrorMessage.value = null
    }

    fun onSearchQueryChange(query: String) {
        val currentState = _uiState.value

        _uiState.value = currentState.copy(
            searchQuery = query,
            filteredOffers = filterOffers(
                offers = currentState.offers,
                query = query,
                city = currentState.selectedCity,
                modality = currentState.selectedModality
            )
        )
    }

    fun onCityFilterChange(city: String) {
        val currentState = _uiState.value

        _uiState.value = currentState.copy(
            selectedCity = city,
            filteredOffers = filterOffers(
                offers = currentState.offers,
                query = currentState.searchQuery,
                city = city,
                modality = currentState.selectedModality
            )
        )
    }

    fun onModalityFilterChange(modality: String) {
        val currentState = _uiState.value

        _uiState.value = currentState.copy(
            selectedModality = modality,
            filteredOffers = filterOffers(
                offers = currentState.offers,
                query = currentState.searchQuery,
                city = currentState.selectedCity,
                modality = modality
            )
        )
    }

    fun clearFilters() {
        val currentOffers = _uiState.value.offers

        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            selectedCity = "",
            selectedModality = "",
            filteredOffers = currentOffers
        )
    }

    private fun filterOffers(
        offers: List<Offer>,
        query: String,
        city: String,
        modality: String
    ): List<Offer> {
        return offers.filter { offer ->
            val matchesQuery =
                query.isBlank() ||
                        offer.title.contains(query, ignoreCase = true) ||
                        offer.company.contains(query, ignoreCase = true) ||
                        offer.location.contains(query, ignoreCase = true) ||
                        offer.requiredSkills.any { it.contains(query, ignoreCase = true) }

            val matchesCity =
                city.isBlank() || offer.location.equals(city, ignoreCase = true)

            val matchesModality =
                modality.isBlank() || offer.modality.equals(modality, ignoreCase = true)

            matchesQuery && matchesCity && matchesModality
        }
    }

    fun getTechnicalTestAnswer(offerId: String): String? {
        return technicalTestAnswers[technicalTestKey(offerId)]
    }
}