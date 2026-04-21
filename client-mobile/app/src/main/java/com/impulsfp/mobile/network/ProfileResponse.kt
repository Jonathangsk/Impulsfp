/**
 * Model de resposta retornat pel servidor amb la informació
 * del perfil d'un usuari.
 *
 * Aquesta classe representa les dades rebudes des del backend
 * quan es consulta el perfil de l'usuari autenticat.
 *
 * Alguns camps són opcionals i poden arribar amb valor `null`
 * si encara no han estat informats per l'usuari.
 *
 * Habitualment aquesta resposta es transforma posteriorment
 * en un model intern de l'aplicació, com ara `UserProfile`.
 *
 * En tractar-se d'una `data class`, Kotlin genera automàticament
 * mètodes útils com `copy()`, `equals()`, `hashCode()` i `toString()`.
 *
 * @property username Nom d'usuari únic dins del sistema
 * @property name Nom real de l'usuari
 * @property surname Cognoms de l'usuari
 * @property email Correu electrònic associat al compte
 * @property phoneNumber Número de telèfon de contacte
 * @property city Ciutat de residència
 * @property bio Descripció personal o professional
 * @property cycle Cicle formatiu cursat o actual
 * @property skills Llista d'habilitats de l'usuari
 * @property experienceLevel Nivell d'experiència professional
 * @property languages Idiomes coneguts
 * @property preferredRoles Rols professionals preferits
 * @property preferredLocation Ubicació laboral preferida
 * @property availability Disponibilitat laboral
 * @property portfolio Enllaç al portafolis o projectes personals
 *
 * @author abenitez
 */
data class ProfileResponse(
    val username: String,
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String?,
    val city: String?,
    val bio: String?,
    val cycle: String?,
    val skills: List<String>?,
    val experienceLevel: String?,
    val languages: List<String>?,
    val preferredRoles: List<String>?,
    val preferredLocation: String?,
    val availability: String?,
    val portfolio: String?,
)