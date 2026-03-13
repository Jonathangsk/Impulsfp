package com.impulsfp.mobile.data

object SessionData {

    var currentUser: User? = null

    fun logout() {
        currentUser = null
    }

    fun isLoggedIn(): Boolean {
        return currentUser != null
    }
}