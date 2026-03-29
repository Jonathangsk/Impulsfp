package com.impulsfp.mobile.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.impulsfp.mobile.data.ProfileRepository

/**
 * ViewModel del perfil d'usuari.
 *
 * Carrega el perfil des del repositori fake i permet actualitzar-lo.
 */
class ProfileViewModel : ViewModel() {

    var profile by mutableStateOf(ProfileRepository.getProfile())
        private set

    fun refreshProfile() {
        profile = ProfileRepository.getProfile()
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
    ) {
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
    }
}

/**
 * Helper per convertir text separat per comes a llista.
 */
private fun String.toListFromCommaText(): List<String> {
    return split(",")
        .map { it.trim() }
        .filter { it.isNotBlank() }
}