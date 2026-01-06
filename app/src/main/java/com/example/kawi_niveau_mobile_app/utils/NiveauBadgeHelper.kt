package com.example.kawi_niveau_mobile_app.utils

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.data.enums.NiveauDifficulte

object NiveauBadgeHelper {

    fun setupNiveauBadge(textView: TextView, niveau: NiveauDifficulte, context: Context) {
        // Définir le texte
        textView.text = "${niveau.getIcon()} ${niveau.displayName}"
        
        // Définir les couleurs et le background selon le niveau
        when (niveau) {
            NiveauDifficulte.DEBUTANT -> {
                textView.setBackgroundResource(R.drawable.niveau_badge_debutant)
                textView.setTextColor(ContextCompat.getColor(context, R.color.niveau_debutant_text))
            }
            NiveauDifficulte.INTERMEDIAIRE -> {
                textView.setBackgroundResource(R.drawable.niveau_badge_intermediaire)
                textView.setTextColor(ContextCompat.getColor(context, R.color.niveau_intermediaire_text))
            }
            NiveauDifficulte.AVANCE -> {
                textView.setBackgroundResource(R.drawable.niveau_badge_avance)
                textView.setTextColor(ContextCompat.getColor(context, R.color.niveau_avance_text))
            }
            NiveauDifficulte.EXPERT -> {
                textView.setBackgroundResource(R.drawable.niveau_badge_expert)
                textView.setTextColor(ContextCompat.getColor(context, R.color.niveau_expert_text))
            }
        }
    }

    fun getNiveauColor(niveau: NiveauDifficulte, context: Context): Int {
        return when (niveau) {
            NiveauDifficulte.DEBUTANT -> ContextCompat.getColor(context, R.color.niveau_debutant)
            NiveauDifficulte.INTERMEDIAIRE -> ContextCompat.getColor(context, R.color.niveau_intermediaire)
            NiveauDifficulte.AVANCE -> ContextCompat.getColor(context, R.color.niveau_avance)
            NiveauDifficulte.EXPERT -> ContextCompat.getColor(context, R.color.niveau_expert)
        }
    }

    fun getNiveauBackgroundColor(niveau: NiveauDifficulte, context: Context): Int {
        return when (niveau) {
            NiveauDifficulte.DEBUTANT -> ContextCompat.getColor(context, R.color.niveau_debutant_bg)
            NiveauDifficulte.INTERMEDIAIRE -> ContextCompat.getColor(context, R.color.niveau_intermediaire_bg)
            NiveauDifficulte.AVANCE -> ContextCompat.getColor(context, R.color.niveau_avance_bg)
            NiveauDifficulte.EXPERT -> ContextCompat.getColor(context, R.color.niveau_expert_bg)
        }
    }
}