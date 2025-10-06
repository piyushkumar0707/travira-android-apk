package com.example.travira.api

import retrofit2.Response
import retrofit2.http.*

interface TraViraApiService {
    
    @POST("mobile/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>
    
    @POST("mobile/location/update")
    suspend fun updateLocation(
        @Header("Authorization") token: String,
        @Body location: LocationUpdate
    ): Response<LocationResponse>
    
    @POST("mobile/panic/alert")
    suspend fun sendPanicAlert(
        @Header("Authorization") token: String,
        @Body alert: PanicAlert
    ): Response<PanicResponse>
    
    @GET("mobile/tourist/status")
    suspend fun getTouristStatus(
        @Header("Authorization") token: String
    ): Response<TouristStatusResponse>

    // Verify tourist and obtain unique ID
    @GET("mobile/tourist/verify")
    suspend fun verifyTourist(
        @Header("Authorization") token: String
    ): Response<VerifyTouristResponse>
    
    @GET("tourists")
    suspend fun getAllTourists(
        @Header("Authorization") token: String
    ): Response<TouristListResponse>
    
    @GET("incidents")
    suspend fun getIncidents(
        @Header("Authorization") token: String
    ): Response<IncidentListResponse>
    
    @GET("health")
    suspend fun getSystemHealth(): Response<SystemHealthResponse>
    
    @GET("test")
    suspend fun testConnection(): Response<SimpleTestResponse>
}
