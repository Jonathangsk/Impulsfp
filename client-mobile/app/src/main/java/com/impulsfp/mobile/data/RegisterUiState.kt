package com.impulsfp.mobile.data

/**
 * Estat de la interfície d'usuari per a la pantalla de registre.
 *
 * Aquesta classe agrupa totes les dades introduïdes per l'usuari,
 * l'estat del procés de registre i els possibles errors de validació
 * o errors retornats pel servidor.
 *
 * S'utilitza habitualment juntament amb un ViewModel per mantenir
 * la UI reactiva i actualitzar la pantalla segons els canvis d'estat.
 *
 * En tractar-se d'una `data class`, permet copiar l'estat fàcilment
 * mitjançant `copy()` per actualitzar només els camps necessaris.
 *
 * @property username Nom d'usuari introduït al formulari
 * @property usernameError Missatge d'error associat al nom d'usuari
 * @property name Nom de l'usuari
 * @property surname Cognoms de l'usuari
 * @property email Correu electrònic introduït
 * @property password Contrasenya introduïda
 * @property confirmPassword Confirmació de la contrasenya
 * @property serverError Missatge d'error retornat pel servidor
 * @property phoneNumber Número de telèfon de contacte
 * @property city Ciutat de residència
 * @property bio Descripció personal o professional
 * @property cycle Cicle formatiu cursat o actual
 * @property skillsText Text amb habilitats introduïdes per l'usuari
 * @property experienceLevel Nivell d'experiència indicat
 * @property languagesText Text amb idiomes introduïts
 * @property preferredRolesText Text amb rols professionals preferits
 * @property preferredLocation Ubicació laboral preferida
 * @property availability Disponibilitat laboral
 * @property portfolio Enllaç al portafolis o projectes personals
 * @property isLoading Indica si el registre està en procés
 * @property registerSuccess Indica si el registre s'ha completat correctament
 * @property nameError Missatge d'error associat al nom
 * @property surnameError Missatge d'error associat als cognoms
 * @property emailError Missatge d'error associat al correu electrònic
 * @property passwordError Missatge d'error associat a la contrasenya
 * @property confirmPasswordError Missatge d'error associat a la confirmació de contrasenya
 *
 * @author abenitez
 */
data class RegisterUiState(
    val username: String = "",
    val usernameError: String? = null,
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val serverError: String? = null,
    val phoneNumber: String = "",
    val city: String = "",
    val bio: String = "",
    val cycle: String = "",
    val skillsText: String = "",
    val experienceLevel: String = "",
    val languagesText: String = "",
    val preferredRolesText: String = "",
    val preferredLocation: String = "",
    val availability: String = "",
    val portfolio: String = "",
    val isLoading: Boolean = false,
    val registerSuccess: Boolean = false,

    val nameError: String? = null,
    val surnameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)