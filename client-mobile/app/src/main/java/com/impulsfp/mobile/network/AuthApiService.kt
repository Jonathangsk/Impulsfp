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
 */


interface AuthApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(
        @Query("sessionId") sessionId: String
    ): Response<LogoutResponse>
}