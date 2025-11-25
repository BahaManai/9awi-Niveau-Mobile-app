package com.example.kawi_niveau_mobile_app.data.responses

data class ProfileResponse(
    val id: Long,
    val email: String,
    val provider: String,
    val emailVerified: Boolean,
    val firstName: String?,
    val lastName: String?,
    val dateOfBirth: String?,
    val profileImage: String?,
    val role: String,
    val createdAt: Long,
    val phoneNumber: String?
)
