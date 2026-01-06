package com.example.kawi_niveau_mobile_app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Long,
    val email: String,
    val token: String,
    val firstName: String?,
    val lastName: String?,
    val role: String,
    val profileImage: String?,
    val dateOfBirth: String?,
    val phoneNumber: String?,
    val provider: String?,
    val emailVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val domaineSpecialisation: String?
)
