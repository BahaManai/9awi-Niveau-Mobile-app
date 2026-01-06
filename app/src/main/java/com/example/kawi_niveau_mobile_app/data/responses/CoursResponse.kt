package com.example.kawi_niveau_mobile_app.data.responses

import com.example.kawi_niveau_mobile_app.data.enums.NiveauDifficulte

data class CoursResponse(
    val id: Long,
    val titre: String,
    val description: String,
    val createdAt: Long,
    val updatedAt: Long,
    val archived: Boolean,
    val archivedAt: Long?,
    val categorie: String?,
    val thumbnailUrl: String?,
    val formateurId: Long,
    val formateurNom: String,
    val niveauDifficulte: String,
    val niveauDifficulteDisplay: String?
) {
    fun getNiveauEnum(): NiveauDifficulte {
        return NiveauDifficulte.fromString(niveauDifficulte)
    }
}
