package com.example.kawi_niveau_mobile_app.data.enums

enum class NiveauDifficulte(val displayName: String) {
    DEBUTANT("Débutant"),
    INTERMEDIAIRE("Intermédiaire"),
    AVANCE("Avancé"),
    EXPERT("Expert");

    companion object {
        fun fromString(value: String): NiveauDifficulte {
            return values().find { it.name == value } ?: DEBUTANT
        }
    }

    fun getBadgeColor(): String {
        return when (this) {
            DEBUTANT -> "#10B981" // Vert
            INTERMEDIAIRE -> "#F59E0B" // Orange
            AVANCE -> "#EF4444" // Rouge
            EXPERT -> "#8B5CF6" // Violet
        }
    }

    fun getBadgeColorLight(): String {
        return when (this) {
            DEBUTANT -> "#D1FAE5" // Vert clair
            INTERMEDIAIRE -> "#FEF3C7" // Orange clair
            AVANCE -> "#FEE2E2" // Rouge clair
            EXPERT -> "#EDE9FE" // Violet clair
        }
    }

    fun getIcon(): String {
        return when (this) {
            DEBUTANT -> "🟢"
            INTERMEDIAIRE -> "🟡"
            AVANCE -> "🔴"
            EXPERT -> "🟣"
        }
    }

    fun getDescription(): String {
        return when (this) {
            DEBUTANT -> "Aucun prérequis, concepts de base"
            INTERMEDIAIRE -> "Connaissances de base requises"
            AVANCE -> "Expérience significative nécessaire"
            EXPERT -> "Maîtrise complète du domaine"
        }
    }
}