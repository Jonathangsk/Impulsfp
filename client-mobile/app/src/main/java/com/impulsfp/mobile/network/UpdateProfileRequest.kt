package com.impulsfp.mobile.network

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