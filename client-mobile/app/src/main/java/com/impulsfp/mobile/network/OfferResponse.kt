package com.impulsfp.mobile.network

data class OfferResponse(
    val id: Int,
    val title: String,
    val description: String,
    val companyName: String,
    val location: String,
    val modality: String,
    val contractType: String,
    val salary: Double?,
    val skills: List<String>,
    val state: String,
    val creationDate: String,
    val applicantsCount: Int?,
    val cycle: String?,
    val testType: String?,
    val testQuestion: String?,
    val codeSnippet: String?,
    val options: String?
)
