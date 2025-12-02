package com.example.kawi_niveau_mobile_app.data.requests

data class CoursRequest(
    val titre: String,
    val description: String? = null,
    val categorie: String? = null,
    val thumbnailUrl: String? = null
)
