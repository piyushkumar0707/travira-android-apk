package com.example.travira.data.model

data class SafetyScore(
    val touristId: String,
    val score: Int,
    val status: String,
    val flags: List<String>
)
