package com.example.safeguard.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val user_id: Int,
    val email: String,
    val full_name: String,
    val role: String // 'admin' atau 'officer' [cite: 397]
)

@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val user: User? = null
)