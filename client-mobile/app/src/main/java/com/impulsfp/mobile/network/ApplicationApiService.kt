package com.impulsfp.mobile.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApplicationsApiService {

    @POST("applications/{offerId}/apply")
    suspend fun applyToOffer(
        @Path("offerId") offerId: String,
        @Query("sessionId") sessionId: String
    ): Response<MessageResponse>

    @GET("applications/my")
    suspend fun getMyApplications(
        @Query("sessionId") sessionId: String
    ): Response<List<ApplicationResponse>>
}