package com.impulsfp.mobile.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Interfície encarregada de definir les peticions HTTP relacionades
 * amb les candidatures de l'usuari.
 *
 * Aquesta interfície utilitza Retrofit per comunicar-se amb el backend
 * i gestionar les operacions d'inscripció a ofertes i recuperació
 * de candidatures enviades.
 *
 * @author abenitez
 */
interface ApplicationsApiService {

    /**
     * Envia una candidatura a una oferta.
     *
     * Realitza una petició POST al servidor amb la informació necessària
     * per registrar la candidatura de l'usuari autenticat.
     *
     * @param sessionId Identificador de sessió de l'usuari autenticat
     * @param request Dades de la candidatura que es vol enviar
     *
     * @return Resposta del servidor amb el resultat de l'operació
     */
    @POST("applications/apply")
    suspend fun applyToOffer(
        @Query("sessionId") sessionId: String,
        @Body request: ApplyRequest
    ): Response<MessageResponse>

    /**
     * Recupera les candidatures de l'usuari autenticat.
     *
     * Realitza una petició GET al servidor per obtenir el llistat
     * de candidatures associades a la sessió activa.
     *
     * @param sessionId Identificador de sessió de l'usuari autenticat
     *
     * @return Resposta del servidor amb la llista de candidatures
     */
    @GET("applications/my")
    suspend fun getMyApplications(
        @Query("sessionId") sessionId: String
    ): Response<List<ApplicationResponse>>
}