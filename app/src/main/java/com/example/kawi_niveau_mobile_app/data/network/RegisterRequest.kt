package com.example.kawi_niveau_mobile_app.data.network

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val email: String,
    val password: String,
    val phoneNumber: String? = null
)