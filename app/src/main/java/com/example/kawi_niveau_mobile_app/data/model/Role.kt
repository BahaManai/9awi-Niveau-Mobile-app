package com.example.kawi_niveau_mobile_app.data.model

enum class Role {
    ETUDIANT,
    FORMATEUR;

    companion object {
        fun fromString(role: String?): Role? {
            return when (role?.uppercase()) {
                "ETUDIANT" -> ETUDIANT
                "FORMATEUR" -> FORMATEUR
                else -> null
            }
        }
    }
}
