package com.impulsfp.mobile.network

/**
 * Model de petició utilitzat per actualitzar la informació
 * del perfil de l'usuari autenticat.
 *
 * Aquesta classe conté totes les dades editables del perfil
 * que s'envien al servidor quan l'usuari desa els canvis
 * realitzats des de la pantalla d'edició.
 *
 * Inclou informació personal, dades acadèmiques,
 * habilitats i preferències professionals.
 *
 * En tractar-se d'una `data class`, Kotlin genera automàticament
 * mètodes útils com `copy()`, `equals()`, `hashCode()` i `toString()`.
 *
 * @property city Ciutat de residència
 * @property bio Descripció personal o professional
 * @property availability Disponibilitat laboral
 * @property skills Llista d'habilitats de l'usuari
 * @property languages Idiomes coneguts
 * @property preferredRoles Rols professionals preferits
 * @property preferredLocation Ubicació laboral preferida
 * @property portfolio Enllaç al portafolis o projectes personals
 * @property phoneNumber Número de telèfon de contacte
 * @property cycle Cicle formatiu cursat o actual
 * @property experienceLevel Nivell d'experiència professional
 * @property name Nom real de l'usuari
 * @property surname Cognoms de l'usuari
 *
 * @author abenitez
 */
data class UpdateProfileRequest(
    val city: String,
    val bio: String,
    val availability: String,
    val skills: List<String>,
    val languages: List<String>,
    val preferredRoles: List<String>,
    val preferredLocation: String,
    val portfolio: String,
    val phoneNumber: String,
    val cycle: String,
    val experienceLevel: String,
    val name: String,
    val surname: String,
)