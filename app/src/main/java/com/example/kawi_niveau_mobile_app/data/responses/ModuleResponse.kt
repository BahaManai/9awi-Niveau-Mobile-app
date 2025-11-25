package com.example.kawi_niveau_mobile_app.data.responses

data class ModuleResponse(
    val id: Long,
    val titre: String,
    val contenu: String,
    val ordre: Int,
    val createdAt: Long,
    val updatedAt: Long,
    val coursId: Long
)
