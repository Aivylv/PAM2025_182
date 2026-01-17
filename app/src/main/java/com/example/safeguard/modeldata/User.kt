package com.example.safeguard.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val user_id: Int,
    val full_name: String,
    val role: String,
    val email: String? = null,
    val password: String? = null
)

@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val user: User? = null
)