package com.impulsfp.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impulsfp.mobile.communications.AuthController
import com.impulsfp.mobile.data.SessionData
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de menú.
 * Gestopma les accions disponibles un cop l'usuari ha iniciat sessió,
 * especialment el tancament de sessió
 */
class MenuViewModel(
    private val authController: AuthController = AuthController()
) : ViewModel() {


    /**
     * Tanca la sessió de l'usuari actual.
     *
     * Si hi ha sessionId, intenta enviar el logout al servidor.
     * Tant si el servidor respon correctament com si falla la connexió,
     * es neteja la sessió local i es continua el flux de sortida.
     *
     * @param onFinished Acció a executar quan el procés de logout ha acabat.
     */
    fun logout(onFinished: () -> Unit) {
        val sessionId = SessionData.getSessionId()

        if (sessionId == null) {
            SessionData.logout()
            onFinished()
            return
        }

        viewModelScope.launch {
            authController.logout(sessionId)
            SessionData.logout()
            onFinished()
        }
    }
}