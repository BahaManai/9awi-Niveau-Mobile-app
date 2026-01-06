package com.example.kawi_niveau_mobile_app.data.responses

data class ParcoursProgressionStatsResponse(
    val totalInscrits: Int,
    val termines: Int,
    val enCours: Int,
    val certificats: Int,
    val progressionMoyenne: Int
)