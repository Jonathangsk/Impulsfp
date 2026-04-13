package com.impulsfp.mobile.network

data class ProfileResponse(
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
    val avatarId: Int?
)