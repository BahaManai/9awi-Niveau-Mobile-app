package com.example.kawi_niveau_mobile_app.data.responses

data class QuizAttemptResponse(
    val id: Long,
    val score: Double,
    val datePassed: Long,
    val reponsesCorrectes: Int,
    val nombreQuestions: Int
)
