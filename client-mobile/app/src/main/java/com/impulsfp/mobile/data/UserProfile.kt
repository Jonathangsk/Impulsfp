package com.impulsfp.mobile.data

/**
 * Model del perfil visible de l'usuari.
 *
 * Aquest model és separat de User perquè User representa
 * la sessió autenticada, mentre que aquesta classe UserProfile
 * representa les dades del perfil que es mostren i editen a la UI
 */

data class UserProfile(
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
    val avatarId: Int = 0
)
