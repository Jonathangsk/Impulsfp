package com.impulsfp.mobile.network

/**
 * Model utilitzat per representar respostes simples del backend
 * que només contenen un missatge informatiu.
 *
 * Aquesta classe s'utilitza principalment en operacions com:
 * - aplicació a ofertes
 * - confirmacions d'accions
 * - respostes de validació
 *
 * Exemple de resposta JSON:
 *
 * {
 *   "message": "Aplicació enviada correctament"
 * }
 *
 * Retrofit i Gson transformen automàticament aquest JSON
 * en una instància de [MessageResponse].
 *
 * @property message Missatge retornat pel servidor
 *
 * @author abenitez
 */
data class MessageResponse(
    val message: String
)