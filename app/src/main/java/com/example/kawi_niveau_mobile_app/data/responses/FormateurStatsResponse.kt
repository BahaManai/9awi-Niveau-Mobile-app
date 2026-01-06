package com.example.kawi_niveau_mobile_app.data.responses

data class FormateurStatsResponse(
    val totalCours: Int,
    val coursActifs: Int,
    val coursArchives: Int,
    val niveauxStats: List<NiveauStatsResponse>
)

data class NiveauStatsResponse(
    val niveau: String,
    val displayName: String,
    val count: Int,
    val percentage: Int
)