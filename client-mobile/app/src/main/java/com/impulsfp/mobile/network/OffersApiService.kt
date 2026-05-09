package com.impulsfp.mobile.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.Path
interface OffersApiService {

    @GET("offers")
    suspend fun getOffers(
        @Query("sessionId") sessionId: String
    ): Response<List<OfferResponse>>

}