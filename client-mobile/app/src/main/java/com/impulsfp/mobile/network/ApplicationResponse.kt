package com.impulsfp.mobile.network

data class ApplicationResponse(
    val id: Int,
    val offerTitle: String,
    val companyName: String,
    val location: String,
    val status: String,
    val appliedAt: String
)