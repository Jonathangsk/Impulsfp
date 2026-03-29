package com.impulsfp.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impulsfp.mobile.communications.AuthController
import com.impulsfp.mobile.data.LoginUiState
import com.impulsfp.mobile.data.SessionData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authController: AuthController = AuthController()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun login() {
        val username = _uiState.value.username.trim()
        val password = _uiState.value.password.trim()

        val error = validateCredentials(username, password)

        if (error != null) {
            _uiState.value = _uiState.value.copy(errorMessage = error)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val result = authController.login(username, password)

            result.onSuccess { user ->
                if (isDesktopOnlyRole(user.role)) {
                    SessionData.currentUser = null
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        loginSuccess = false,
                        errorMessage = "Aquest compte pertany a una empresa. Per iniciar sessió, has d'accedir des de l'aplicació d'escriptori."
                    )
                    return@onSuccess
                }

                SessionData.currentUser = user
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = true,
                    errorMessage = null
                )
            }.onFailure { error ->
                SessionData.currentUser = null
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = false,
                    errorMessage = error.message ?: "Error desconegut"
                )
            }
        }
    }

    fun validateCredentials(username: String, password: String): String? {
        return when {
            username.isBlank() && password.isBlank() ->
                "Cal introduir l'usuari i la contrasenya per iniciar sessió."
            username.isBlank() ->
                "Cal introduir el nom d'usuari."
            password.isBlank() ->
                "Cal introduir la contrasenya."
            else -> null
        }
    }

    private fun isDesktopOnlyRole(role: String?): Boolean {
        return when (role?.trim()?.uppercase()) {
            "COMPANY", "EMPRESA" -> true
            else -> false
        }
    }

    fun resetLoginSuccess() {
        _uiState.value = _uiState.value.copy(loginSuccess = false)
    }
}