package com.impulsfp.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impulsfp.mobile.data.LoginUiState
import com.impulsfp.mobile.data.SessionData
import com.impulsfp.mobile.communications.AuthController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de login.
 *
 * Gestiona:
 * - Els valors introduïts per l'usuari
 * - La validació de credencials
 * - La comunicació amb el servidor per fer login
 * - L'actualització de l'estat de la UI
 *
 * @property authController Controlador encarregat de gestionar
 * les operacions d'autenticació contra el servidor.
 *
 * @author abenitez
 */
class LoginViewModel(
    private val authController: AuthController = AuthController()
) : ViewModel() {


    private val _uiState = MutableStateFlow(LoginUiState())

    /**
     * Estat observable de la pantalla de login.
     */
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Actualitza el username introduït a la UI.
     *
     * @param value Nou valor del camp username
     */
    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value)
    }

    /**
     * Actualitza la contrasenya introduïda a la UI.
     *
     * @param value Nou valor del camp password
     */
    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    /**
     * Valida les dades introduïdes i, si són correctes,
     * inicia el procés de login contra el servidor.
     *
     * Si l'autenticació és correcta, guarda l'usuari a [SessionData]
     * i actualutza l'estat perquè la interfície pugui navegar.
     * Si hi ha error, actualitza l'estat amb el missatge corresponent.
     */
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

            val result = authController.login (username, password)

            result.onSuccess { user ->
                SessionData.currentUser = user
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = true
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "Error desconegut"
                )
            }
        }
    }

    /**
     * Comprova que username i password no estiguin buits.
     *
     * @param username Nom d'usuari introduït per l'usuari
     * @param password Contrasenya introduïda per l'usuari
     *
     * @return Missatge d'error si la validació falla, o null si és correcta.
     */
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

    /**
     * Reinicia l'estat de loginSuccess després de navegar,
     * per evitar navegacions repetides.
     */
    fun resetLoginSuccess() {
        _uiState.value = _uiState.value.copy (loginSuccess = false)
    }

}