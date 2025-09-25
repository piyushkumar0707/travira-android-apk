package com.example.travira.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object ApiClient {
    
    // Using emulator's special host IP (10.0.2.2 maps to host machine)
    private const val BASE_URL = "http://10.0.2.2:5000/api/"
    
    // Alternative approaches tried:
    // private const val BASE_URL = "http://localhost:5000/api/"  // With port forwarding
    // private const val BASE_URL = "http://192.168.29.150:5000/api/"  // Direct IP
    
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    // Add a comprehensive interceptor for debugging
    private val debugInterceptor = okhttp3.Interceptor { chain ->
        val request = chain.request()
        android.util.Log.d("API", "Making request to: ${request.url}")
        android.util.Log.d("API", "Request method: ${request.method}")
        android.util.Log.d("API", "Request headers: ${request.headers}")
        
        try {
            val startTime = System.currentTimeMillis()
            val response = chain.proceed(request)
            val endTime = System.currentTimeMillis()
            
            android.util.Log.d("API", "Response received in ${endTime - startTime}ms")
            android.util.Log.d("API", "Response code: ${response.code}")
            android.util.Log.d("API", "Response message: ${response.message}")
            android.util.Log.d("API", "Response headers: ${response.headers}")
            
            response
        } catch (e: Exception) {
            android.util.Log.e("API", "Network error occurred", e)
            android.util.Log.e("API", "Error type: ${e.javaClass.simpleName}")
            android.util.Log.e("API", "Error message: ${e.message}")
            android.util.Log.e("API", "Cause: ${e.cause}")
            throw e
        }
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(debugInterceptor)
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val apiService: TraViraApiService = retrofit.create(TraViraApiService::class.java)
    
    // Helper function to add Bearer token to requests
    fun createAuthHeader(token: String): String {
        return "Bearer $token"
    }
    
    // Test basic connectivity without Retrofit
    suspend fun testRawConnection(): String {
        return try {
            // Use the existing Retrofit API service instead of raw OkHttp
            val response = apiService.testConnection()
            
            if (response.isSuccessful) {
                val testResponse = response.body()
                android.util.Log.d("API", "Raw response: ${testResponse?.message}")
                "✓ Raw connection successful: ${testResponse?.message ?: "Connected"}"
            } else {
                android.util.Log.e("API", "Raw connection failed: ${response.code()} - ${response.message()}")
                "✗ Raw connection failed: ${response.code()} - ${response.message()}"
            }
        } catch (e: Exception) {
            android.util.Log.e("API", "Raw connection error", e)
            "✗ Raw connection error: ${e.message}"
        }
    }
}
