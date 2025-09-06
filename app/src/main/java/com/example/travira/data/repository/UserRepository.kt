package com.example.travira.data.repository

import com.example.travira.data.model.SafetyScore
import com.example.travira.data.model.Tourist
import kotlinx.coroutines.delay
import kotlin.random.Random

class UserRepository {
    // Dummy register
    suspend fun registerTourist(name: String): Tourist {
        delay(700) // simulate network
        return Tourist(
            id = "tourist_${Random.nextInt(1000,9999)}",
            name = name,
            vcHash = "0xDUMMYHASH${Random.nextInt(100,999)}",
            verified = true
        )
    }

    // Dummy safety score
    suspend fun getSafetyScore(touristId: String): SafetyScore {
        delay(700)
        val score = Random.nextInt(40, 95)
        val status = when {
            score >= 75 -> "safe"
            score >= 50 -> "moderate"
            else -> "risky"
        }
        return SafetyScore(touristId = touristId, score = score, status = status, flags = listOf("geofence"))
    }
}
