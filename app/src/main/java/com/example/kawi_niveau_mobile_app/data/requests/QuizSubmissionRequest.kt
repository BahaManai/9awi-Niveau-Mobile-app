package com.example.kawi_niveau_mobile_app.data.requests

data class QuizSubmissionRequest(
    val reponses: Map<Long, String>, // questionId -> reponse choisie
    val tempsPasse: Int // en secondes
)
