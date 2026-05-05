package com.impulsfp.mobile.network

data class ApplyRequest(
    val offerId: String,
    val answer: String? // null si no hi ha test
)