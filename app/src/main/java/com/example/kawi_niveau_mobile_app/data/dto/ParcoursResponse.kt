package com.example.kawi_niveau_mobile_app.data.dto

data class ParcoursResponse(
    val id: Long,
    val titre: String?,
    val description: String?,
    val niveauDifficulte: String?, // Changé de niveau à niveauDifficulte
    val dureeEstimeeHeures: Int?, // Changé de dureeEstimee à dureeEstimeeHeures
    val imageUrl: String?,
    val isPublished: Boolean?, // Changé de actif à isPublished
    val dateCreation: String?,
    val formateurId: Long?,
    val formateurNom: String?
)

data class ParcoursProgressionStatsResponse(
    val parcoursId: Long,
    val parcoursTitre: String,
    val totalInscrits: Int,
    val nombreTermines: Int,
    val nombreEnCours: Int,
    val nombreCertificats: Int,
    val progressionMoyenne: Double
)