package com.impulsfp.mobile.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApplicationsApiService {

    @GET("applications")
    suspend fun getApplications(
        @Query("sessionId") sessionId: String
    ): Response<List<ApplicationResponse>>
}