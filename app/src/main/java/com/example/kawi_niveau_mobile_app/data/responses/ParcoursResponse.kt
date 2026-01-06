package com.example.kawi_niveau_mobile_app.data.responses

data class ParcoursResponse(
    val id: Long,
    val titre: String,
    val description: String?,
    val thumbnailUrl: String?,
    val categorie: String?,
    val niveauDifficulte: String,
    val dureeEstimeeHeures: Int?,
    val isPublished: Boolean,
    val createdAt: Long,
    val formateurNom: String,
    val nombreEtapes: Int,
    val nombreInscriptions: Int,
    val nombreCompletions: Int,
    val progressionMoyenne: Double?
)