package com.impulsfp.mobile.network

data class TechnicalTestResponse(
    val id: Int,
    val type: String,
    val question: String,
    val codeSnippet: String?,
    val options: List<String>
)
