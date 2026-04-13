package com.impulsfp.mobile.ui

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impulsfp.mobile.data.ProfileRepository
import com.impulsfp.mobile.communications.ProfileController
import com.impulsfp.mobile.data.SessionData
import kotlinx.coroutines.launch


class ProfileViewModel : ViewModel() {

    var profile by mutableStateOf(ProfileRepository.getProfile())
        private set

    var nameError by mutableStateOf<String?>(null)
        private set

    var surnameError by mutableStateOf<String?>(null)
        private set

    var emailError by mutableStateOf<String?>(null)
        private set

    var cycleError by mutableStateOf<String?>(null)
        private set

    private val profileController = ProfileController()

    fun refreshProfile(sessionId: String) {
        viewModelScope.launch {
            val result = profileController.getProfile(sessionId)

            result.onSuccess {
                profile = it
            }.onFailure {
                // de moment pots ignorar error o posar log
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
        avatarId: Int
    ): Boolean {
        val isValid = validateProfile(
            name = name,
            surname = surname,
            email = email,
            cycle = cycle
        )

        if (!isValid) return false

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
            portfolio = portfolio.trim(),
            avatarId = avatarId
        )

        ProfileRepository.updateProfile(updatedProfile)
        profile = updatedProfile
        clearAllErrors()

        return true
    }

    private fun validateProfile(
        name: String,
        surname: String,
        email: String,
        cycle: String
    ): Boolean {
        nameError = if (name.isBlank()) "El nom és obligatori." else null

        surnameError = if (surname.isBlank()) "Els cognoms són obligatoris." else null

        emailError = when {
            email.isBlank() -> "El correu electrònic és obligatori."
            !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() ->
                "El correu electrònic no és vàlid."
            else -> null
        }

        cycleError = if (cycle.isBlank()) "El cicle formatiu és obligatori." else null

        return nameError == null &&
                surnameError == null &&
                emailError == null &&
                cycleError == null
    }

    fun clearNameError() {
        nameError = null
    }

    fun clearSurnameError() {
        surnameError = null
    }

    fun clearEmailError() {
        emailError = null
    }

    fun clearCycleError() {
        cycleError = null
    }

    private fun clearAllErrors() {
        nameError = null
        surnameError = null
        emailError = null
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