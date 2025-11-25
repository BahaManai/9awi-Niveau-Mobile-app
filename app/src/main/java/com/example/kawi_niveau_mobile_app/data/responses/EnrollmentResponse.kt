package com.example.kawi_niveau_mobile_app.data.responses

data class EnrollmentResponse(
    val id: Long,
    val userId: Long,
    val coursId: Long,
    val coursTitle: String,
    val coursDescription: String,
    val enrolledAt: Long,
    val progress: Float,
    val lastAccessedAt: Long,
    val totalLecons: Int,
    val completedLecons: Int
)
