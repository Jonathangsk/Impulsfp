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

/**
 * ViewModel encarregat de gestionar l'estat i la lògica
 * de la pantalla de login.
 *
 * Aquesta classe controla els valors introduïts per l'usuari,
 * valida les credencials abans d'enviar-les al servidor i
 * coordina el procés d'autenticació a través de [AuthController].
 *
 * També actualitza l'estat de la interfície segons el resultat
 * del login, gestiona els missatges d'error i desa l'usuari
 * autenticat a [SessionData] quan la sessió és correcta.
 *
 * @property authController Controlador encarregat de la comunicació
 * amb el backend per a les operacions d'autenticació
 *
 * @author abenitez
 */
class LoginViewModel(
    private val authController: AuthController = AuthController()
) : ViewModel() {

    /**
     * Estat intern mutable de la pantalla de login.
     */
    private val _uiState = MutableStateFlow(LoginUiState())

    /**
     * Estat públic observable de la pantalla de login.
     *
     * S'exposa com a [StateFlow] només de lectura perquè la UI
     * pugui reaccionar als canvis sense modificar-lo directament.
     */
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Actualitza el nom d'usuari introduït per l'usuari.
     *
     * @param value Nou valor del camp nom d'usuari
     */
    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value)
    }

    /**
     * Actualitza la contrasenya introduïda per l'usuari.
     *
     * @param value Nou valor del camp contrasenya
     */
    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    /**
     * Inicia el procés de login.
     *
     * Aquest mètode:
     * - recupera les credencials introduïdes
     * - valida que els camps obligatoris estiguin informats
     * - llança la petició d'autenticació al backend
     * - actualitza l'estat de càrrega, error o èxit segons el resultat
     *
     * Si el rol retornat correspon a un compte només disponible
     * a l'aplicació d'escriptori, es bloqueja l'accés des del mòbil
     * i es mostra un missatge informatiu.
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

    /**
     * Valida les credencials introduïdes per l'usuari.
     *
     * Comprova que tant el nom d'usuari com la contrasenya
     * estiguin informats abans d'iniciar el procés de login.
     *
     * @param username Nom d'usuari introduït
     * @param password Contrasenya introduïda
     *
     * @return Missatge d'error si la validació falla o `null` si és correcta
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
     * Indica si un rol només pot accedir des de l'aplicació d'escriptori.
     *
     * Actualment es considera restringit el rol d'empresa.
     *
     * @param role Rol de l'usuari autenticat
     *
     * @return `true` si el rol no hauria de poder iniciar sessió des del mòbil
     */
    private fun isDesktopOnlyRole(role: String?): Boolean {
        return when (role?.trim()?.uppercase()) {
            "COMPANY", "EMPRESA" -> true
            else -> false
        }
    }

    /**
     * Reinicia l'estat d'èxit del login.
     *
     * S'utilitza habitualment després de navegar a una altra pantalla
     * per evitar que el senyal de login correcte es torni a processar.
     */
    fun resetLoginSuccess() {
        _uiState.value = _uiState.value.copy(loginSuccess = false)
    }
}