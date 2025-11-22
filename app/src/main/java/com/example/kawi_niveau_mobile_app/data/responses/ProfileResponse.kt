package com.example.kawi_niveau_mobile_app.data.responses

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    val id: Long,
    val email: String,
    val provider: String,
    val emailVerified: Boolean,
    val firstName: String?,
    val lastName: String?,
    val dateOfBirth: String?,
    val phoneNumber: String?,
    val profileImage: String?,
    val createdAt: Long?
)
