package com.example.travira.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

// ===== REQUEST MODELS =====
data class LoginRequest(
    val username: String,
    val password: String,
    val deviceId: String = "android_emulator_demo"
)

data class LocationUpdate(
    val lat: Double,
    val lng: Double,
    val accuracy: Float,
    val timestamp: Long = System.currentTimeMillis()
)

data class PanicAlert(
    val lat: Double?,
    val lng: Double?,
    val message: String = "Emergency assistance needed!"
)

// ===== RESPONSE MODELS =====
data class AuthResponse(
    val success: Boolean,
    val token: String,
    val user: User,
    val apiBaseUrl: String,
    val error: String? = null
)

@Parcelize
data class User(
    val id: String,
    val username: String,
    val email: String,
    val role: String,
    val status: String
) : Parcelable

data class LocationResponse(
    val success: Boolean,
    val message: String,
    val tourist: Tourist? = null,
    val error: String? = null
)

data class PanicResponse(
    val success: Boolean,
    val incidentId: String,
    val message: String,
    val estimatedResponse: String,
    val error: String? = null
)

data class TouristStatusResponse(
    val status: String,
    val message: String? = null,
    val tourist: Tourist? = null,
    val safetyScore: Int = 85,
    val recommendations: List<String> = emptyList(),
    val safetyTips: List<String> = emptyList(),
    val nearbyServices: List<NearbyService> = emptyList()
)

@Parcelize
data class Tourist(
    val id: String,
    val userId: String? = null,
    val name: String,
    val location: Location,
    val safetyScore: Int,
    val status: String,
    val lastUpdated: String? = null,
    val accuracy: Float? = null
) : Parcelable

@Parcelize
data class Location(
    val lat: Double,
    val lng: Double
) : Parcelable

data class NearbyService(
    val type: String,
    val distance: String,
    val contact: String
)

data class TouristListResponse(
    val tourists: List<Tourist>,
    val summary: TouristSummary? = null
)

data class TouristSummary(
    val total: Int,
    val active: Int,
    val highRisk: Int,
    val averageSafetyScore: Int
)

data class IncidentListResponse(
    val incidents: List<Incident>,
    val summary: IncidentSummary? = null
)

@Parcelize
data class Incident(
    val id: String,
    val type: String,
    val location: String,
    val severity: String,
    val status: String,
    val touristId: String? = null,
    val tourist: String? = null,
    val assignedOfficer: String? = null,
    val createdAt: String,
    val description: String,
    val coordinates: Location? = null
) : Parcelable

data class IncidentSummary(
    val total: Int,
    val open: Int,
    val resolved: Int,
    val highSeverity: Int
)

data class SystemHealthResponse(
    val overall: String,
    val services: List<ServiceStatus>,
    val timestamp: String
)

data class ServiceStatus(
    val name: String,
    val status: String,
    val uptime: String,
    val responseTime: String
)

data class SimpleTestResponse(
    val status: String,
    val message: String,
    val timestamp: String,
    val server: String
)

// ===== ERROR MODELS =====
data class ApiError(
    val error: String,
    val message: String? = null,
    val details: String? = null
)
