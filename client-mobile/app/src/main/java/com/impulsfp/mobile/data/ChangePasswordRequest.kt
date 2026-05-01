package com.impulsfp.mobile.data

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)