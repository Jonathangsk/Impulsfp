package com.impulsfp.mobile.ui

import androidx.lifecycle.ViewModel
import com.impulsfp.mobile.data.SessionData

class MenuViewModel : ViewModel() {
    fun logout() {
        SessionData.logout()
    }
}