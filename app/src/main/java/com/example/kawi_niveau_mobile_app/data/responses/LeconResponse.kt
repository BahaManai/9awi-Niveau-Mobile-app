package com.example.kawi_niveau_mobile_app.data.responses

data class LeconResponse(
    val id: Long,
    val titre: String,
    val typeContenu: String, // TEXTE, PDF, IMAGE, VIDEO
    val contenuTexte: String?,
    val fichierUrl: String?,
    val ordre: Int?,
    val duree: Int?, // Durée en minutes
    val createdAt: Long,
    val updatedAt: Long,
    val moduleId: Long
)
