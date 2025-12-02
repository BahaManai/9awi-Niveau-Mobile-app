package com.example.kawi_niveau_mobile_app.data.local.entity

import com.example.kawi_niveau_mobile_app.data.responses.ProfileResponse

fun ProfileResponse.toUserEntity(token: String): UserEntity {
    return UserEntity(
        id = this.id,
        email = this.email,
        token = token,
        firstName = this.firstName,
        lastName = this.lastName,
        role = this.role,
        profileImage = this.profileImage,
        dateOfBirth = this.dateOfBirth,
        phoneNumber = this.phoneNumber,
        provider = this.provider,
        emailVerified = this.emailVerified,
        createdAt = this.createdAt
    )
}
