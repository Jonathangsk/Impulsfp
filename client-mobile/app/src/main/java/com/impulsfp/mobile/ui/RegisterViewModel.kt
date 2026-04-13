package com.impulsfp.mobile.ui

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impulsfp.mobile.communications.AuthController
import com.impulsfp.mobile.data.RegisterUiState
import com.impulsfp.mobile.data.SessionData
import com.impulsfp.mobile.network.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val authController = AuthController()

    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(
            username = value,
            usernameError = null
        )
    }

    fun onNameChange(value: String) {
        _uiState.value = _uiState.value.copy(
            name = value,
            nameError = null
        )
    }

    fun onSurnameChange(value: String) {
        _uiState.value = _uiState.value.copy(
            surname = value,
            surnameError = null
        )
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(
            email = value,
            emailError = null
        )
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(
            password = value,
            passwordError = null
        )
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = value,
            confirmPasswordError = null
        )
    }

    fun onPhoneNumberChange(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value)
    }

    fun onCityChange(value: String) {
        _uiState.value = _uiState.value.copy(city = value)
    }

    fun onBioChange(value: String) {
        _uiState.value = _uiState.value.copy(bio = value)
    }

    fun onCycleChange(value: String) {
        _uiState.value = _uiState.value.copy(cycle = value)
    }

    fun onSkillsTextChange(value: String) {
        _uiState.value = _uiState.value.copy(skillsText = value)
    }

    fun onExperienceLevelChange(value: String) {
        _uiState.value = _uiState.value.copy(experienceLevel = value)
    }

    fun onLanguagesChange(value: String) {
        _uiState.value = _uiState.value.copy(languagesText = value)
    }

    fun onPreferredRolesTextChange(value: String) {
        _uiState.value = _uiState.value.copy(preferredRolesText = value)
    }

    fun onPreferredLocationChange(value: String) {
        _uiState.value = _uiState.value.copy(preferredLocation = value)
    }

    fun onAvailabilityChange(value: String) {
        _uiState.value = _uiState.value.copy(availability = value)
    }

    fun onPortfolioChange(value: String) {
        _uiState.value = _uiState.value.copy(portfolio = value)
    }

    fun register() {
        val currentState = _uiState.value

        val validatedState = validateForm(currentState)
        _uiState.value = validatedState

        val isValid = validatedState.usernameError == null &&
                validatedState.nameError == null &&
                validatedState.surnameError == null &&
                validatedState.emailError == null &&
                validatedState.passwordError == null &&
                validatedState.confirmPasswordError == null

        if (!isValid) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            val result = authController.registerStudent(
                RegisterRequest(
                    username = currentState.username.trim(),
                    password = currentState.password,
                    name = currentState.name.trim(),
                    surname = currentState.surname.trim(),
                    email = currentState.email.trim(),
                    phoneNumber = currentState.phoneNumber.trim(),
                    city = currentState.city.trim(),
                    bio = currentState.bio.trim(),
                    cycle = currentState.cycle.trim(),
                    experienceLevel = currentState.experienceLevel.trim(),
                    skills = parseCommaSeparatedList(currentState.skillsText),
                    languages = parseCommaSeparatedList(currentState.languagesText),
                    preferredRoles = parseCommaSeparatedList(currentState.preferredRolesText),
                    preferredLocation = currentState.preferredLocation.trim(),
                    availability = currentState.availability.trim(),
                    portfolio = currentState.portfolio.trim()
                )
            )

            result.onSuccess { user ->
                SessionData.currentUser = user

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    registerSuccess = true
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    serverError = it.message
                )
            }
        }
    }

    private fun validateForm(state: RegisterUiState): RegisterUiState {

        val usernameError = when {
            state.username.isBlank() ->
                "El nom d'usuari és obligatori."

            state.username.length < 4 || state.username.length > 20 ->
                "Ha de tenir entre 4 i 20 caràcters."

            !state.username.matches(Regex("^[A-Za-z0-9]+$")) ->
                "Només pot contenir lletres i números."

            else -> null
        }

        val nameError = if (state.name.isBlank()) {
            "El nom és obligatori."
        } else {
            null
        }

        val surnameError = if (state.surname.isBlank()) {
            "Els cognoms són obligatoris."
        } else {
            null
        }

        val emailError = when {
            state.email.isBlank() -> "El correu electrònic és obligatori."
            !isValidEmail(state.email) -> "El correu electrònic no és vàlid."
            else -> null
        }

        val passwordRequirements = mutableListOf<String>()

        if (state.password.isBlank()) {
            passwordRequirements.add("La contrasenya és obligatòria.")
        } else {
            if (state.password.length < 6) {
                passwordRequirements.add("• mínim 6 caràcters")
            }
            if (!state.password.any { it.isUpperCase() }) {
                passwordRequirements.add("• 1 majúscula")
            }
            if (!state.password.any { it.isLowerCase() }) {
                passwordRequirements.add("• 1 minúscula")
            }
            if (!state.password.any { it.isDigit() }) {
                passwordRequirements.add("• 1 número")
            }
        }

        val passwordError =
            if (passwordRequirements.isEmpty()) {
                null
            } else if (passwordRequirements.first() == "La contrasenya és obligatòria.") {
                "La contrasenya és obligatòria."
            } else {
                "La contrasenya ha de contenir:\n" +
                        passwordRequirements.joinToString("\n")
            }
        val confirmPasswordError = when {
            state.confirmPassword.isBlank() -> "Has de confirmar la contrasenya."
            state.password != state.confirmPassword -> "Les contrasenyes no coincideixen."
            else -> null
        }

        return state.copy(
            usernameError = usernameError,
            nameError = nameError,
            surnameError = surnameError,
            emailError = emailError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError
        )
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    }

    fun resetRegisterSuccess() {
        _uiState.value = _uiState.value.copy(registerSuccess = false)
    }

    fun parseCommaSeparatedList(text: String): List<String> {
        return text
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
    }
}