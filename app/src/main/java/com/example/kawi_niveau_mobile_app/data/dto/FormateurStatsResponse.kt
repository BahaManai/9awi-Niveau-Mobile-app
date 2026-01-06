package com.example.kawi_niveau_mobile_app.data.dto

data class FormateurStatsResponse(
    val totalCours: Int = 0,
    val coursActifs: Int = 0,
    val totalApprenants: Int = 0,
    val tauxReussiteMoyen: Double = 0.0,
    val coursParNiveau: List<CoursParNiveauDto> = emptyList()
)

data class CoursParNiveauDto(
    val niveau: String?,
    val nombre: Int = 0,
    val pourcentage: Double = 0.0
)