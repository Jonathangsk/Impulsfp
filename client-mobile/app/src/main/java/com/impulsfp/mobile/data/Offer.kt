package com.impulsfp.mobile.data

data class Offer(
    val id: String,
    val title: String,
    val description: String,
    val company: String,
    val requiredSkills: List<String>,
    val location: String,
    val modality: String,
    val contractType: String,
    val salary: String? = null,
    val createdAt: String,
    val state: String,
    val applicantsCount: Int = 0
)