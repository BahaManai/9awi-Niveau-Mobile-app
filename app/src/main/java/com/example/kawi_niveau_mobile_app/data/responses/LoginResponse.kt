package com.example.kawi_niveau_mobile_app.data.responses

data class LoginResponse(
    val token: String?,
    val email: String?,
    val role: String?,
    val message: String?
)
