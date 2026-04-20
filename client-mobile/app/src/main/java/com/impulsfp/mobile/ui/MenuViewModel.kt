package com.impulsfp.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impulsfp.mobile.communications.AuthController
import com.impulsfp.mobile.data.SessionData
import kotlinx.coroutines.launch

/**
 * ViewModel encarregat de gestionar les accions del menú d'usuari.
 *
 * Aquesta classe controla les operacions disponibles un cop
 * l'usuari ha iniciat sessió, especialment el procés de
 * tancament de sessió.
 *
 * Utilitza [AuthController] per comunicar-se amb el backend
 * i [SessionData] per netejar la sessió emmagatzemada localment.
 *
 * @property authController Controlador encarregat de comunicar-se
 * amb el servidor per realitzar l'operació de logout
 *
 * @author abenitez
 */
class MenuViewModel(
    private val authController: AuthController = AuthController()
) : ViewModel() {

    /**
     * Tanca la sessió de l'usuari actual.
     *
     * Si existeix un identificador de sessió, intenta notificar
     * el logout al servidor. Tant si l'operació remota és correcta
     * com si falla, es neteja igualment la sessió local i es
     * continua el flux de sortida.
     *
     * Si no hi ha cap sessió activa, es neteja directament
     * l'estat local i s'executa el callback final.
     *
     * @param onFinished Funció que s'executa quan el procés de logout ha finalitzat
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