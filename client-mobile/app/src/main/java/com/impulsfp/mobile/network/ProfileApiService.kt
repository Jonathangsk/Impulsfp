package com.impulsfp.mobile.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Query

interface ProfileApiService {

    @GET("profile")
    suspend fun getProfile(
        @Query("sessionId") sessionId: String
    ): Response<ProfileResponse>

    @HTTP(method = "DELETE", path = "users/me", hasBody = true)
    suspend fun deleteAccount(
        @Query("sessionId") sessionId: String,
        @Body request: DeleteAccountRequest
    ): Response<Map<String, String>>
}