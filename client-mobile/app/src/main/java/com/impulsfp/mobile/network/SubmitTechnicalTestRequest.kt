package com.impulsfp.mobile.network

data class SubmitTechnicalTestRequest(
    val technicalTestId: String,
    val selectedAnswer: String,
    val timeExpired: Boolean = false
)