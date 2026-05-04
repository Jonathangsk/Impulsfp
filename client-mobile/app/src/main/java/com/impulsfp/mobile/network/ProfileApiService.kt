package com.impulsfp.mobile.network

import ProfileResponse
import com.impulsfp.mobile.data.ChangePasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Query

/**
 * Interfície que defineix les crides HTTP relacionades
 * amb la gestió del perfil d'usuari.
 *
 * Aquesta interfície és utilitzada per Retrofit per generar
 * automàticament la implementació encarregada de comunicar-se
 * amb el backend.
 *
 * Endpoints disponibles:
 * - GET users/me
 * - PUT users/me
 * - DELETE users/me
 *
 * Totes les operacions requereixen un identificador de sessió
 * vàlid per autenticar l'usuari.
 *
 * @author abenitez
 */
interface ProfileApiService {

    /**
     * Recupera les dades del perfil de l'usuari autenticat.
     *
     * Fa una petició al servidor i retorna tota la informació
     * disponible del perfil associat a la sessió activa.
     *
     * @param sessionId Identificador de sessió de l'usuari autenticat
     *
     * @return Resposta HTTP amb les dades del perfil
     */
    @GET("users/me")
    suspend fun getProfile(
        @Query("sessionId") sessionId: String
    ): Response<ProfileResponse>

    /**
     * Actualitza les dades del perfil de l'usuari.
     *
     * Envia al servidor la nova informació del perfil
     * per substituir les dades actuals.
     *
     * @param sessionId Identificador de sessió de l'usuari autenticat
     * @param request Dades noves del perfil
     *
     * @return Resposta HTTP amb el resultat de l'actualització
     */
    @PUT("users/me")
    suspend fun updateProfile(
        @Query("sessionId") sessionId: String,
        @Body request: UpdateProfileRequest
    ): Response<UpdateProfileResponse>

    /**
     * Elimina definitivament el compte de l'usuari autenticat.
     *
     * Envia una petició DELETE amb cos per permetre validar
     * la contrasenya abans d'executar l'eliminació.
     *
     * @param sessionId Identificador de sessió de l'usuari autenticat
     * @param request Petició amb la contrasenya de confirmació
     *
     * @return Resposta HTTP amb el missatge del resultat
     */
    @HTTP(method = "DELETE", path = "users/me", hasBody = true)
    suspend fun deleteAccount(
        @Query("sessionId") sessionId: String,
        @Body request: DeleteAccountRequest
    ): Response<Map<String, String>>

    /**
     * Canvia la contrasenya de l'usuari autenticat.
     *
     * Valida la contrasenya actual i actualitza la nova
     * contrasenya al servidor.
     *
     * @param sessionId Identificador de sessió actiu
     * @param request Contrasenya actual i nova contrasenya
     *
     * @return Resposta HTTP amb el resultat de l'operació
     */
    @PATCH("users/password")
    suspend fun changePassword(
        @Query("sessionId") sessionId: String,
        @Body request: ChangePasswordRequest
    ): Response<Map<String, String>>
}