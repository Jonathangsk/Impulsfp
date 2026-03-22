package com.impulsfp.mobile.data

/**
 * Objecte singleton que manté la sessió actual de l'usuari dins de l'app.
 *
 * Guarda en memòria l'usuari autenticat mentre l'aplicació està en execució,
 * permetent accedir a la informació de sessió des de qualsevol part del sistema.
 *
 * @author abenitez
 *
 */
object SessionData {

    /**
     * Usuari actual autenticat.
     *
     * Serà null si no hi ha cap sessió iniciada.
     */
    var currentUser: User? = null

    /**
     * Elimina la sessió actual de l'usuari.
     *
     * Estableix l'usuari actual a null.
     */
    fun logout() {
        currentUser = null
    }

    /**
     * Indica si hi ha un ususari autenticat actualment.
     *
     * @return true si hi ha una sessió activa, false en cas contrari.
     */
    fun isLoggedIn(): Boolean {
        return currentUser != null
    }

    /**
     * Retorna el sessionId de l'usuari actual, si existeix.
     *
     * @return sessionId o null si no hi ha cap usuari autenticat
     */
    fun getSessionId(): String? {
        return currentUser?.sessionId
    }
}