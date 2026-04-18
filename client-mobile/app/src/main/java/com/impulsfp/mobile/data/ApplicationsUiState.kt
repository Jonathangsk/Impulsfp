package com.impulsfp.mobile.data

data class ApplicationsUiState(
    val applications: List<ApplicationUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)