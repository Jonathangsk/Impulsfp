package com.impulsfp.mobile.network

import ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PUT
import retrofit2.http.Query

interface ProfileApiService {

    @GET("users/me")
    suspend fun getProfile(
        @Query("sessionId") sessionId: String
    ): Response<ProfileResponse>

    @PUT("users/me")
    suspend fun updateProfile(
        @Query("sessionId") sessionId: String,
        @Body request: UpdateProfileRequest
    ): Response<UpdateProfileResponse>

    @HTTP(method = "DELETE", path = "users/me", hasBody = true)
    suspend fun deleteAccount(
        @Query("sessionId") sessionId: String,
        @Body request: DeleteAccountRequest
    ): Response<Map<String, String>>
}