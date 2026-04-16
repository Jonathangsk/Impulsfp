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

class ProfileViewModel : ViewModel() {

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

    var isLoading by mutableStateOf(false)
        private set

    var serverError by mutableStateOf<String?>(null)
        private set

    var saveError by mutableStateOf<String?>(null)
        private set

    var saveSuccess by mutableStateOf(false)
        private set

    var nameError by mutableStateOf<String?>(null)
        private set

    var surnameError by mutableStateOf<String?>(null)
        private set

    var cycleError by mutableStateOf<String?>(null)
        private set

    private val profileController = ProfileController()

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

    fun clearNameError() {
        nameError = null
    }

    fun clearSurnameError() {
        surnameError = null
    }

    fun clearCycleError() {
        cycleError = null
    }

    fun clearSaveState() {
        saveError = null
        saveSuccess = false
    }

    private fun clearAllErrors() {
        nameError = null
        surnameError = null
        cycleError = null
    }

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

private fun String.toListFromCommaText(): List<String> {
    return split(",")
        .map { it.trim() }
        .filter { it.isNotBlank() }
}