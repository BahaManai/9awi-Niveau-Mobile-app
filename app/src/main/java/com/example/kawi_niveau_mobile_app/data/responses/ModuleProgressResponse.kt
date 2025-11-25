package com.example.kawi_niveau_mobile_app.data.responses

data class ModuleProgressResponse(
    val id: Long,
    val titre: String,
    val contenu: String,
    val ordre: Int,
    val createdAt: Long,
    val updatedAt: Long,
    val coursId: Long,
    
    // Progression des leçons
    val totalLecons: Int,
    val leconsCompletees: Int,
    val progressionLecons: Float,
    
    // Quiz
    val hasQuiz: Boolean,
    val quizId: Long?,
    val quizTitre: String?,
    val quizPassed: Boolean,
    val bestScore: Double?,
    val totalAttempts: Int
)
