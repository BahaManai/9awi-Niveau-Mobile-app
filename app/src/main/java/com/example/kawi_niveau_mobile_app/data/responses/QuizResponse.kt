package com.example.kawi_niveau_mobile_app.data.responses

data class QuizResponse(
    val id: Long,
    val titre: String,
    val description: String?,
    val moduleId: Long,
    val questions: List<QuestionResponse>?,
    val createdAt: Long,
    val updatedAt: Long
)

data class QuestionResponse(
    val id: Long,
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val ordre: Int?,
    val createdAt: Long
)
