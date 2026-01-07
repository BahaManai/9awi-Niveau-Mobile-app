package com.example.kawi_niveau_mobile_app.data.dto

data class CoursStatsResponse(
    val totalInscrits: Int,
    val progressionMoyenne: Double,
    val tauxReussite: Double,
    val nombreCertificats: Int
)