package com.example.kawi_niveau_mobile_app.data.responses

data class ResultatQuizResponse(
    val id: Long,
    val userId: Long,
    val quizId: Long,
    val quizTitre: String,
    val score: Double,
    val datePassed: Long,
    val nombreQuestions: Int,
    val reponsesCorrectes: Int,
    val tempsPasse: Int,
    val details: List<QuestionResultat>
)

data class QuestionResultat(
    val questionId: Long,
    val question: String,
    val reponseUtilisateur: String,
    val reponseCorrecte: String,
    val correct: Boolean
)
