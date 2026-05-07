package com.impulsfp.mobile.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.Path
// import retrofit2.http.Body

interface OffersApiService {

    @GET("offers")
    suspend fun getOffers(
        @Query("sessionId") sessionId: String
    ): Response<List<OfferResponse>>

    @POST("offers/{offerId}/apply")
    suspend fun applyToOffer(
        @Path("offerId") offerId: String,
        @Query("sessionId") sessionId: String
    ): Response<ApplyOfferResponse>

    // TEA4 - Proves tècniques
    // Recupera una prova tècnica concreta.
    //
    // @GET("technical-tests/{testId}")
    // suspend fun getTechnicalTest(
    //     @Path("testId") testId: String,
    //     @Query("sessionId") sessionId: String
    // ): Response<TechnicalTestResponse>

    // TEA4 - Proves tècniques
    // Envia la resposta de l'alumne a la prova tècnica.
    //
    // @POST("offers/{offerId}/technical-test")
    // suspend fun submitTechnicalTest(
    //     @Path("offerId") offerId: String,
    //     @Query("sessionId") sessionId: String,
    //     @Body request: SubmitTechnicalTestRequest
    // ): Response<SubmitTechnicalTestResponse>
}