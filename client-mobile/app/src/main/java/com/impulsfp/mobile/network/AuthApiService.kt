package com.impulsfp.mobile.network

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Query

/**
 * Interfície que defineix les crides HTTP relacionades amb l'autenticació
 *
 * Aquesta classe és utilitzada per Retrofit per generar automàticament
 * la implementació que farà les peticions al servidor.
 *
 * Endpoints disponibles:
 * -POST /auth/login
 * -POST /auth/logout
 *
 * @author abenitez
 */


interface AuthApiService {

    /**
     * Realitza la petició de login al servidor.
     *
     * @param request Objecte amb les credencials de l'usuari
     * @return Resposta HTTP amb el resultat del login
     */
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    /**
     * Realitza la petició de logout al servidor.
     *
     * @param sessionId Identificador de sessió de l'usuari autenticat
     * @return Resposta HTTP amb el resultat del logout
     */
    @POST("auth/logout")
    suspend fun logout(
        @Query("sessionId") sessionId: String
    ): Response<LogoutResponse>
}