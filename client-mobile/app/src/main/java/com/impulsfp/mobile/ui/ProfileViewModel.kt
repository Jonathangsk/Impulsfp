package com.impulsfp.mobile.ui

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impulsfp.mobile.communications.ProfileController
import com.impulsfp.mobile.data.SessionData
import com.impulsfp.mobile.data.UserProfile
import com.impulsfp.mobile.network.UpdateProfileRequest
import kotlinx.coroutines.launch

/**
 * ViewModel encarregat de gestionar l'estat i la lògica
 * relacionada amb el perfil de l'usuari.
 *
 * Aquesta classe permet:
 * - carregar les dades del perfil des del servidor
 * - actualitzar la informació del perfil
 * - validar els camps obligatoris del formulari
 * - gestionar errors de càrrega i desat
 * - eliminar el compte de l'usuari
 *
 * També manté l'estat observable necessari perquè la interfície
 * pugui reaccionar automàticament als canvis.
 *
 * @property profileController Controlador encarregat de fer les operacions
 * de comunicació relacionades amb el perfil
 *
 * @author abenitez
 */
class ProfileViewModel(
    private val profileController: ProfileController = ProfileController()
) : ViewModel() {

    /**
     * Estat actual del perfil de l'usuari.
     *
     * Inicialment conté un perfil buit fins que es carreguen
     * les dades reals des del servidor.
     */
    var profile by mutableStateOf(
        UserProfile(
            username = "",
            name = "",
            surname = "",
            email = "",
            phoneNumber = "",
            city = "",
            bio = "",
            cycle = "",
            skills = emptyList(),
            experienceLevel = "",
            languages = emptyList(),
            preferredRoles = emptyList(),
            preferredLocation = "",
            availability = "",
            portfolio = ""
        )
    )
        private set

    /**
     * Indica si hi ha una operació en curs, com ara la càrrega
     * o l'actualització del perfil.
     */
    var isLoading by mutableStateOf(false)
        private set

    /**
     * Missatge d'error produït durant la càrrega del perfil
     * des del servidor.
     */
    var serverError by mutableStateOf<String?>(null)
        private set

    /**
     * Missatge d'error produït durant el procés de desat
     * o actualització del perfil.
     */
    var saveError by mutableStateOf<String?>(null)
        private set

    /**
     * Indica si l'operació de desat del perfil s'ha completat
     * correctament.
     */
    var saveSuccess by mutableStateOf(false)
        private set

    /**
     * Missatge d'error de validació associat al camp nom.
     */
    var nameError by mutableStateOf<String?>(null)
        private set

    /**
     * Missatge d'error de validació associat al camp cognoms.
     */
    var surnameError by mutableStateOf<String?>(null)
        private set

    /**
     * Missatge d'error de validació associat al camp
     * cicle formatiu.
     */
    var cycleError by mutableStateOf<String?>(null)
        private set

    /**
     * Carrega o refresca les dades del perfil de l'usuari
     * a partir d'un identificador de sessió.
     *
     * Durant l'operació s'actualitza l'estat de càrrega i,
     * en cas d'error, es desa el missatge corresponent.
     *
     * @param sessionId Identificador de la sessió activa de l'usuari
     */
    fun refreshProfile(sessionId: String) {
        viewModelScope.launch {
            isLoading = true
            serverError = null

            val result = profileController.getProfile(sessionId)

            result.onSuccess {
                profile = it
                isLoading = false
            }.onFailure {
                serverError = it.message ?: "No s'ha pogut carregar el perfil"
                isLoading = false
            }
        }
    }

    /**
     * Desa els canvis del perfil de l'usuari després de validar
     * els camps obligatoris i construir la petició d'actualització.
     *
     * Si la validació falla o no existeix una sessió activa,
     * l'operació no continua.
     *
     * En cas d'èxit:
     * - s'actualitza el perfil local
     * - es netegen els errors de validació
     * - s'indica que el desat ha estat correcte
     * - s'executa la funció de retorn indicada
     *
     * @param name Nom de l'usuari
     * @param surname Cognoms de l'usuari
     * @param email Correu electrònic de l'usuari
     * @param phoneNumber Número de telèfon de l'usuari
     * @param city Ciutat de residència de l'usuari
     * @param bio Biografia o descripció personal
     * @param cycle Cicle formatiu de l'usuari
     * @param skillsText Text amb les habilitats separades per comes
     * @param experienceLevel Nivell d'experiència de l'usuari
     * @param languagesText Text amb els idiomes separats per comes
     * @param preferredRolesText Text amb els rols preferits separats per comes
     * @param preferredLocation Ubicació preferida per treballar
     * @param availability Disponibilitat de l'usuari
     * @param portfolio Enllaç al portafolis de l'usuari
     * @param onSuccess Funció que s'executa si el perfil s'actualitza correctament
     */
    fun saveProfile(
        name: String,
        surname: String,
        email: String,
        phoneNumber: String,
        city: String,
        bio: String,
        cycle: String,
        skillsText: String,
        experienceLevel: String,
        languagesText: String,
        preferredRolesText: String,
        preferredLocation: String,
        availability: String,
        portfolio: String,
        onSuccess: () -> Unit
    ) {
        val isValid = validateProfile(
            name = name,
            surname = surname,
            cycle = cycle
        )

        if (!isValid) return

        val sessionId = SessionData.getSessionId()
        if (sessionId == null) {
            saveError = "No hi ha cap sessió activa"
            return
        }

        val updatedProfile = profile.copy(
            name = name.trim(),
            surname = surname.trim(),
            email = email.trim(),
            phoneNumber = phoneNumber.trim(),
            city = city.trim(),
            bio = bio.trim(),
            cycle = cycle.trim(),
            skills = skillsText.toListFromCommaText(),
            experienceLevel = experienceLevel.trim(),
            languages = languagesText.toListFromCommaText(),
            preferredRoles = preferredRolesText.toListFromCommaText(),
            preferredLocation = preferredLocation.trim(),
            availability = availability.trim(),
            portfolio = portfolio.trim()
        )

        val request = UpdateProfileRequest(
            name = updatedProfile.name,
            surname = updatedProfile.surname,
            phoneNumber = updatedProfile.phoneNumber,
            city = updatedProfile.city,
            bio = updatedProfile.bio,
            cycle = updatedProfile.cycle,
            experienceLevel = updatedProfile.experienceLevel,
            skills = updatedProfile.skills,
            languages = updatedProfile.languages,
            preferredRoles = updatedProfile.preferredRoles,
            preferredLocation = updatedProfile.preferredLocation,
            availability = updatedProfile.availability,
            portfolio = updatedProfile.portfolio
        )

        viewModelScope.launch {
            isLoading = true
            saveError = null
            saveSuccess = false

            profileController.updateProfile(sessionId, request)
                .onSuccess {
                    profile = updatedProfile
                    clearAllErrors()
                    saveError = null
                    saveSuccess = true
                    isLoading = false
                    onSuccess()
                }
                .onFailure {
                    saveError = it.message ?: "No s'ha pogut actualitzar el perfil"
                    saveSuccess = false
                    isLoading = false
                }
        }
    }

    /**
     * Valida els camps obligatoris del perfil.
     *
     * Comprova que el nom, els cognoms i el cicle formatiu
     * no estiguin en blanc. Si algun camp no és vàlid,
     * es desa el missatge d'error corresponent.
     *
     * @param name Nom de l'usuari
     * @param surname Cognoms de l'usuari
     * @param cycle Cicle formatiu de l'usuari
     *
     * @return true si tots els camps obligatoris són vàlids;
     * false en cas contrari
     */
    private fun validateProfile(
        name: String,
        surname: String,
        cycle: String
    ): Boolean {
        nameError = if (name.isBlank()) "El nom és obligatori." else null

        surnameError = if (surname.isBlank()) "Els cognoms són obligatoris." else null

        cycleError = if (cycle.isBlank()) "El cicle formatiu és obligatori." else null

        return nameError == null &&
                surnameError == null &&
                cycleError == null
    }

    /**
     * Neteja l'error associat al camp nom.
     */
    fun clearNameError() {
        nameError = null
    }

    /**
     * Neteja l'error associat al camp cognoms.
     */
    fun clearSurnameError() {
        surnameError = null
    }

    /**
     * Neteja l'error associat al camp cicle formatiu.
     */
    fun clearCycleError() {
        cycleError = null
    }

    /**
     * Reinicia l'estat relacionat amb el desat del perfil,
     * eliminant l'error existent i marcant el desat com
     * no completat.
     */
    fun clearSaveState() {
        saveError = null
        saveSuccess = false
    }

    /**
     * Neteja tots els errors de validació del formulari.
     */
    private fun clearAllErrors() {
        nameError = null
        surnameError = null
        cycleError = null
    }

    /**
     * Elimina el compte de l'usuari autenticat.
     *
     * Si l'operació es completa correctament, també es tanca
     * la sessió localment. En cas d'error, s'executa la funció
     * de retorn amb el missatge corresponent.
     *
     * @param sessionId Identificador de la sessió activa
     * @param password Contrasenya de confirmació de l'usuari
     * @param onSuccess Funció que s'executa si el compte s'elimina correctament
     * @param onError Funció que s'executa si es produeix un error durant l'operació
     */
    fun deleteAccount(
        sessionId: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            profileController.deleteAccount(sessionId, password)
                .onSuccess {
                    SessionData.logout()
                    onSuccess()
                }
                .onFailure {
                    onError(it.message ?: "No s'ha pogut eliminar el compte")
                }
        }
    }
}

/**
 * Converteix un text amb valors separats per comes en una llista
 * de cadenes netes.
 *
 * Cada element:
 * - es separa per comes
 * - s'eliminen els espais sobrants als extrems
 * - es descarten els valors buits
 *
 * @receiver Cadena de text amb valors separats per comes
 *
 * @return Llista de cadenes netes i no buides
 */
private fun String.toListFromCommaText(): List<String> {
    return split(",")
        .map { it.trim() }
        .filter { it.isNotBlank() }
}