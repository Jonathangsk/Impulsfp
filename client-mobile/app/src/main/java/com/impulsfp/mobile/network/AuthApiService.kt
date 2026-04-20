package com.impulsfp.mobile.network

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Query

/**
 * Interfície que defineix les crides HTTP relacionades amb
 * l'autenticació i el registre d'usuaris.
 *
 * Aquesta interfície és utilitzada per Retrofit per generar
 * automàticament la implementació encarregada de comunicar-se
 * amb el servidor.
 *
 * Endpoints disponibles:
 * - POST /auth/login
 * - POST /auth/logout
 * - POST /auth/register/student
 *
 * Cada mètode retorna un objecte [Response] amb la resposta HTTP
 * corresponent, permetent validar codis d'estat i contingut.
 *
 * @author abenitez
 */
interface AuthApiService {

    /**
     * Realitza la petició d'inici de sessió al servidor.
     *
     * Envia les credencials de l'usuari i retorna la informació
     * necessària per crear la sessió autenticada.
     *
     * @param request Objecte amb el nom d'usuari i la contrasenya
     *
     * @return Resposta HTTP amb el resultat del login
     */
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    /**
     * Realitza la petició de tancament de sessió.
     *
     * Envia l'identificador de sessió actiu per invalidar-lo
     * al servidor i finalitzar la sessió de l'usuari.
     *
     * @param sessionId Identificador de sessió de l'usuari autenticat
     *
     * @return Resposta HTTP amb el resultat del logout
     */
    @POST("auth/logout")
    suspend fun logout(
        @Query("sessionId") sessionId: String
    ): Response<LogoutResponse>

    /**
     * Realitza la petició de registre d'un nou alumne.
     *
     * Envia al servidor totes les dades necessàries del formulari
     * de registre i retorna la resposta amb la nova sessió creada.
     *
     * @param request Dades introduïdes al formulari de registre
     *
     * @return Resposta HTTP amb el resultat del registre
     */
    @POST("auth/register/student")
    suspend fun registerStudent(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>
}