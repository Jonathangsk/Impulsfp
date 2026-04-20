package com.impulsfp.mobile.network

/**
 * Model de petició utilitzat per registrar un nou usuari alumne.
 *
 * Aquesta classe encapsula totes les dades introduïdes al formulari
 * de registre que s'envien al backend per crear un nou compte.
 *
 * Inclou informació personal, dades acadèmiques i preferències
 * professionals per completar el perfil inicial de l'usuari.
 *
 * En tractar-se d'una `data class`, Kotlin genera automàticament
 * mètodes útils com `copy()`, `equals()`, `hashCode()` i `toString()`.
 *
 * @property username Nom d'usuari únic desitjat
 * @property password Contrasenya del nou compte
 * @property name Nom real de l'usuari
 * @property surname Cognoms de l'usuari
 * @property email Correu electrònic associat al compte
 * @property phoneNumber Número de telèfon de contacte
 * @property city Ciutat de residència
 * @property bio Descripció personal o professional
 * @property cycle Cicle formatiu cursat o actual
 * @property experienceLevel Nivell d'experiència professional
 * @property skills Llista d'habilitats de l'usuari
 * @property languages Idiomes coneguts
 * @property preferredRoles Rols professionals preferits
 * @property preferredLocation Ubicació laboral preferida
 * @property availability Disponibilitat laboral
 * @property portfolio Enllaç al portafolis o projectes personals
 *
 * @author abenitez
 */
data class RegisterRequest(
    val username: String,
    val password: String,
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
    val city: String,
    val bio: String,
    val cycle: String,
    val experienceLevel: String,
    val skills: List<String>,
    val languages: List<String>,
    val preferredRoles: List<String>,
    val preferredLocation: String,
    val availability: String,
    val portfolio: String
)