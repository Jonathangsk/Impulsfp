package com.impulsfp.mobile.data

data class RegisterUiState(
    val username: String = "",
    val usernameError: String? = null,    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val serverError: String? = null,
    val phoneNumber: String = "",
    val city: String = "",
    val bio: String = "",
    val cycle: String = "",
    val skillsText: String = "",
    val experienceLevel: String = "",
    val languagesText: String = "",
    val preferredRolesText: String = "",
    val preferredLocation: String = "",
    val availability: String = "",
    val portfolio: String = "",
    val isLoading: Boolean = false,
    val registerSuccess: Boolean = false,

    val nameError: String? = null,
    val surnameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)