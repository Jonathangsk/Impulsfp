package com.impulsfp.mobile.ui

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

/**
 * ViewModel encarregat de gestionar l'estat i la lògica
 * del procés de registre d'usuaris.
 *
 * Aquesta classe controla:
 * - l'estat del formulari de registre
 * - l'actualització dels camps introduïts per l'usuari
 * - la validació de les dades obligatòries
 * - la comunicació amb el controlador d'autenticació
 * - el tractament dels resultats del registre
 *
 * També exposa un estat observable perquè la interfície
 * pugui reaccionar als canvis de manera automàtica.
 *
 * @property authController Controlador encarregat de gestionar
 * les operacions d'autenticació i registre
 */
class RegisterViewModel(
    private val authController: AuthController = AuthController()
) : ViewModel() {


    /**
     * Flux mutable intern que conté l'estat actual
     * del formulari de registre.
     */
    private val _uiState = MutableStateFlow(RegisterUiState())

    /**
     * Flux públic de només lectura que exposa l'estat actual
     * del formulari de registre a la interfície d'usuari.
     */
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    /**
     * Actualitza el valor del camp nom d'usuari
     * i reinicia el seu error de validació.
     *
     * @param value Nou valor introduït al camp
     */
    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(
            username = value,
            usernameError = null
        )
    }

    /**
     * Actualitza el valor del camp nom
     * i reinicia el seu error de validació.
     *
     * @param value Nou valor introduït al camp
     */
    fun onNameChange(value: String) {
        _uiState.value = _uiState.value.copy(
            name = value,
            nameError = null
        )
    }

    /**
     * Actualitza el valor del camp cognoms
     * i reinicia el seu error de validació.
     *
     * @param value Nou valor introduït al camp
     */
    fun onSurnameChange(value: String) {
        _uiState.value = _uiState.value.copy(
            surname = value,
            surnameError = null
        )
    }

    /**
     * Actualitza el valor del camp correu electrònic
     * i reinicia el seu error de validació.
     *
     * @param value Nou valor introduït al camp
     */
    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(
            email = value,
            emailError = null
        )
    }

    /**
     * Actualitza el valor del camp contrasenya
     * i reinicia el seu error de validació.
     *
     * @param value Nou valor introduït al camp
     */
    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(
            password = value,
            passwordError = null
        )
    }

    /**
     * Actualitza el valor del camp de confirmació
     * de contrasenya i reinicia el seu error de validació.
     *
     * @param value Nou valor introduït al camp
     */
    fun onConfirmPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = value,
            confirmPasswordError = null
        )
    }

    /**
     * Actualitza el valor del camp telèfon.
     *
     * @param value Nou valor introduït al camp
     */
    fun onPhoneNumberChange(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value)
    }

    /**
     * Actualitza el valor del camp ciutat.
     *
     * @param value Nou valor introduït al camp
     */
    fun onCityChange(value: String) {
        _uiState.value = _uiState.value.copy(city = value)
    }

    /**
     * Actualitza el valor del camp biografia.
     *
     * @param value Nou valor introduït al camp
     */
    fun onBioChange(value: String) {
        _uiState.value = _uiState.value.copy(bio = value)
    }

    /**
     * Actualitza el valor del camp cicle formatiu.
     *
     * @param value Nou valor introduït al camp
     */
    fun onCycleChange(value: String) {
        _uiState.value = _uiState.value.copy(cycle = value)
    }

    /**
     * Actualitza el text del camp d'habilitats
     * separades per comes.
     *
     * @param value Nou valor introduït al camp
     */
    fun onSkillsTextChange(value: String) {
        _uiState.value = _uiState.value.copy(skillsText = value)
    }

    /**
     * Actualitza el valor del camp nivell d'experiència.
     *
     * @param value Nou valor introduït al camp
     */
    fun onExperienceLevelChange(value: String) {
        _uiState.value = _uiState.value.copy(experienceLevel = value)
    }

    /**
     * Actualitza el text del camp idiomes
     * separats per comes.
     *
     * @param value Nou valor introduït al camp
     */
    fun onLanguagesChange(value: String) {
        _uiState.value = _uiState.value.copy(languagesText = value)
    }

    /**
     * Actualitza el text del camp rols preferits
     * separats per comes.
     *
     * @param value Nou valor introduït al camp
     */
    fun onPreferredRolesTextChange(value: String) {
        _uiState.value = _uiState.value.copy(preferredRolesText = value)
    }

    /**
     * Actualitza el valor del camp ubicació preferida.
     *
     * @param value Nou valor introduït al camp
     */
    fun onPreferredLocationChange(value: String) {
        _uiState.value = _uiState.value.copy(preferredLocation = value)
    }

    /**
     * Actualitza el valor del camp disponibilitat.
     *
     * @param value Nou valor introduït al camp
     */
    fun onAvailabilityChange(value: String) {
        _uiState.value = _uiState.value.copy(availability = value)
    }

    /**
     * Actualitza el valor del camp portfolio.
     *
     * @param value Nou valor introduït al camp
     */
    fun onPortfolioChange(value: String) {
        _uiState.value = _uiState.value.copy(portfolio = value)
    }

    /**
     * Inicia el procés de registre d'un nou usuari.
     *
     * Primer valida el formulari i actualitza l'estat amb els errors
     * detectats. Si les dades són correctes, fa la petició de registre
     * al controlador d'autenticació.
     *
     * En cas d'èxit:
     * - desa l'usuari registrat a la sessió actual
     * - marca el registre com a completat
     *
     * En cas d'error:
     * - actualitza l'estat amb el missatge d'error del servidor
     */
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

    /**
     * Valida les dades del formulari de registre.
     *
     * Comprova:
     * - que els camps obligatoris estiguin informats
     * - que el nom d'usuari compleixi el format establert
     * - que el correu electrònic sigui vàlid
     * - que la contrasenya compleixi els requisits mínims
     * - que la confirmació de contrasenya coincideixi
     *
     * @param state Estat actual del formulari
     *
     * @return Una còpia de l'estat amb els missatges d'error
     * de validació corresponents
     */
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

    /**
     * Comprova si una adreça de correu electrònic
     * té un format vàlid.
     *
     * Primer intenta validar-la utilitzant el patró estàndard
     * d'Android. Si aquest no està disponible, aplica una
     * expressió regular alternativa.
     *
     * @param email Correu electrònic a validar
     *
     * @return true si el correu és vàlid; false en cas contrari
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS
            ?.matcher(email.trim())
            ?.matches()
            ?: Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(email.trim())
    }

    /**
     * Reinicia l'estat que indica que el registre
     * s'ha completat correctament.
     *
     * Aquest mètode s'utilitza habitualment després
     * de gestionar la navegació associada a un registre exitós.
     */
    fun resetRegisterSuccess() {
        _uiState.value = _uiState.value.copy(registerSuccess = false)
    }

    /**
     * Converteix un text amb valors separats per comes
     * en una llista de cadenes netes i no buides.
     *
     * Cada element:
     * - es separa per comes
     * - s'eliminen els espais sobrants
     * - es descarten els valors en blanc
     *
     * @param text Text que conté elements separats per comes
     *
     * @return Llista d'elements processats
     */
    fun parseCommaSeparatedList(text: String): List<String> {
        return text
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
    }
}