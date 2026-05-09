package com.impulsfp.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impulsfp.mobile.communications.ApplicationsController
import com.impulsfp.mobile.data.ApplicationsUiState
import com.impulsfp.mobile.data.SessionData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encarregat de gestionar la lògica de negoci
 * relacionada amb les candidatures de l'usuari.
 *
 * Aquesta classe s'encarrega de recuperar les candidatures
 * enviades per l'usuari autenticat i actualitzar l'estat
 * de la interfície mitjançant [ApplicationsUiState].
 *
 * També controla els diferents estats de càrrega i els
 * possibles errors produïts durant la comunicació amb el servidor.
 *
 * Utilitza corrutines i StateFlow per gestionar l'estat
 * reactiu de la pantalla de candidatures.
 *
 * @author abenitez
 */
class ApplicationsViewModel(
    private val applicationsController: ApplicationsController = ApplicationsController()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApplicationsUiState())
    val uiState: StateFlow<ApplicationsUiState> = _uiState.asStateFlow()

    init {
        loadApplications()
    }

    fun loadApplications() {
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

            val result = applicationsController.getMyApplications(sessionId)

            result.onSuccess { applications ->
                _uiState.value = _uiState.value.copy(
                    applications = applications,
                    isLoading = false,
                    errorMessage = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "Error en carregar les candidatures"
                )
            }
        }
    }
}