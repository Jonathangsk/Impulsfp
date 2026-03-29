package com.impulsfp.mobile.data

/**
 * Repositori temporal amb dades mock.
 *
 * Ara mateix guarda les dades en memòria.
 * Més endevant es podrà substituir per crides reals al servidor.
 */
object ProfileRepository {

    private var currentProfile = UserProfile(
        name = "Alba",
        surname = "Benitez",
        email = "alba@impulsfp.cat",
        phoneNumber = "600123123",
        city = "Barcelona",
        bio = "Estudiant de DAM interessada en desenvolupament mòbil",
        cycle = "DAM",
        skills = listOf("Kotlin", "SQL", "Java"),
        experienceLevel = "Junior",
        languages = listOf("Català", "Castellà", "Anglès"),
        preferredRoles = listOf("Android Developer", "Backend Junior"),
        preferredLocation = "Barcelona",
        availability = "Matins",
        portfolio = "https://github.com/albeniri",
        avatarId = 1
    )

    fun getProfile(): UserProfile {
        return currentProfile
    }

    fun updateProfile(newProfile: UserProfile) {
        currentProfile = newProfile
    }
}