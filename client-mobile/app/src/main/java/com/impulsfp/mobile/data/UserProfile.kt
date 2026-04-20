package com.impulsfp.mobile.data

/**
 * Model de dades que representa el perfil visible d'un usuari.
 *
 * Aquesta classe conté la informació personal, acadèmica i professional
 * que es mostra i es pot editar des de la interfície de l'aplicació.
 *
 * Es diferencia de la classe `User`, ja que `User` representa
 * l'usuari autenticat i la seva sessió activa, mentre que
 * `UserProfile` emmagatzema exclusivament les dades del perfil.
 *
 * En tractar-se d'una `data class`, Kotlin proporciona automàticament
 * funcionalitats com `copy()`, `equals()`, `hashCode()` i `toString()`.
 *
 * @property username Nom d'usuari únic dins del sistema
 * @property name Nom real de l'usuari
 * @property surname Cognoms de l'usuari
 * @property email Correu electrònic associat al compte
 * @property phoneNumber Número de telèfon de contacte
 * @property city Ciutat de residència
 * @property bio Descripció personal o professional de l'usuari
 * @property cycle Cicle formatiu cursat o completat
 * @property skills Llista d'habilitats tècniques o personals
 * @property experienceLevel Nivell d'experiència professional
 * @property languages Idiomes coneguts per l'usuari
 * @property preferredRoles Rols professionals preferits
 * @property preferredLocation Ubicació laboral preferida
 * @property availability Disponibilitat per incorporar-se o treballar
 * @property portfolio Enllaç al portafolis o projectes personals
 *
 * @author abenitez
 */
data class UserProfile(
    val username: String,
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
    val city: String,
    val bio: String,
    val cycle: String,
    val skills: List<String>,
    val experienceLevel: String,
    val languages: List<String>,
    val preferredRoles: List<String>,
    val preferredLocation: String,
    val availability: String,
    val portfolio: String,
)