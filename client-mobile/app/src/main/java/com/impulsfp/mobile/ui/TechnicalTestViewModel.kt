package com.impulsfp.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impulsfp.mobile.communications.TechnicalTestController
import com.impulsfp.mobile.data.SessionData
import com.impulsfp.mobile.network.TechnicalTestResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TechnicalTestViewModel(
    private val technicalTestController: TechnicalTestController = TechnicalTestController()
) : ViewModel() {

    private val _technicalTest = MutableStateFlow<TechnicalTestResponse?>(null)
    val technicalTest: StateFlow<TechnicalTestResponse?> = _technicalTest.asStateFlow()

    private val _selectedAnswer = MutableStateFlow("")
    val selectedAnswer: StateFlow<String> = _selectedAnswer.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    // TEA4 - Proves tècniques
    // Estat futur per controlar el temporitzador de 2 minuts.
    //
    // private val _remainingSeconds = MutableStateFlow(120)
    // val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()
    //
    // private val _timeExpired = MutableStateFlow(false)
    // val timeExpired: StateFlow<Boolean> = _timeExpired.asStateFlow()

    fun onAnswerSelected(answer: String) {
        _selectedAnswer.value = answer
        _errorMessage.value = null
    }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    /*
    TEA4 - Proves tècniques

    Inicia el temporitzador de 2 minuts.
    Quan arribi a 0, marcarà la prova com a expirada.

    fun startTimer() {
        viewModelScope.launch {
            while (_remainingSeconds.value > 0) {
                kotlinx.coroutines.delay(1000)
                _remainingSeconds.value -= 1
            }

            _timeExpired.value = true
        }
    }

    Carrega la prova tècnica associada a una oferta.

    fun loadTechnicalTest(testId: String) {
        val sessionId = SessionData.getSessionId()

        if (sessionId.isNullOrBlank()) {
            _errorMessage.value = "No hi ha cap sessió activa"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = technicalTestController.getTechnicalTest(
                testId = testId,
                sessionId = sessionId
            )

            result.onSuccess { test ->
                _technicalTest.value = test
                _isLoading.value = false
            }.onFailure { error ->
                _errorMessage.value =
                    error.message ?: "No s'ha pogut carregar la prova tècnica"
                _isLoading.value = false
            }
        }
    }

    Envia la resposta seleccionada per l'alumne.

    fun submitTechnicalTest(
        offerId: String,
        testId: String,
        onCompleted: () -> Unit
    ) {
        val sessionId = SessionData.getSessionId()

        if (sessionId.isNullOrBlank()) {
            _errorMessage.value = "No hi ha cap sessió activa"
            return
        }

        if (_selectedAnswer.value.isBlank()) {
            _errorMessage.value = "Has de seleccionar una resposta"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null

            val result = technicalTestController.submitTechnicalTest(
                offerId = offerId,
                sessionId = sessionId,
                technicalTestId = testId,
                selectedAnswer = _selectedAnswer.value
            )

            result.onSuccess { message ->
                _successMessage.value = message
                _isLoading.value = false
                onCompleted()
            }.onFailure { error ->
                _errorMessage.value =
                    error.message ?: "No s'ha pogut enviar la prova tècnica"
                _isLoading.value = false
            }
        }
    }
    */
}