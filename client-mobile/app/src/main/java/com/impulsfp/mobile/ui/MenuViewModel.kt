package com.impulsfp.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impulsfp.mobile.communications.AuthController
import com.impulsfp.mobile.data.SessionData
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de menú d'usuari.
 *
 *
 * Gestiona les accions disponibles un cop l'usuari ha iniciat sessió,
 * especialment el procés de tancament de sessió (logout).
 *
 * @property authController Controler encarregat de comunicar-se amb el servidor
 * per realitzar l'operació de logout
 *
 * @author abenitez
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
     * @param onFinished Funció que s'executa quan el procés de logout ha acabat.
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