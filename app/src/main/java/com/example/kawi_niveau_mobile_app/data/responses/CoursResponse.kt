package com.example.kawi_niveau_mobile_app.data.responses

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
    val formateurNom: String
)
