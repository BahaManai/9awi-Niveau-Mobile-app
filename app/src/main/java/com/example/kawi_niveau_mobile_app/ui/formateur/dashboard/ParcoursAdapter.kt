package com.example.kawi_niveau_mobile_app.ui.formateur.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.data.dto.ParcoursResponse
import com.google.android.material.card.MaterialCardView

class ParcoursAdapter(
    private val onParcoursClick: (ParcoursResponse) -> Unit
) : ListAdapter<ParcoursResponse, ParcoursAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_parcours_dashboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onParcoursClick)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardParcours: MaterialCardView = itemView.findViewById(R.id.cardParcours)
        private val textViewTitre: TextView = itemView.findViewById(R.id.textViewTitre)
        private val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        private val textViewNiveau: TextView = itemView.findViewById(R.id.textViewNiveau)
        private val textViewDuree: TextView = itemView.findViewById(R.id.textViewDuree)
        private val textViewStatut: TextView = itemView.findViewById(R.id.textViewStatut)

        fun bind(parcours: ParcoursResponse, onParcoursClick: (ParcoursResponse) -> Unit) {
            textViewTitre.text = parcours.titre ?: "Titre non défini"
            textViewDescription.text = parcours.description ?: "Description non disponible"
            
            val niveauDifficulte = parcours.niveauDifficulte ?: "DEBUTANT"
            textViewNiveau.text = when(niveauDifficulte) {
                "DEBUTANT" -> "🟢 Débutant"
                "INTERMEDIAIRE" -> "🟡 Intermédiaire"
                "AVANCE" -> "🔴 Avancé"
                else -> niveauDifficulte
            }
            
            textViewDuree.text = "${parcours.dureeEstimeeHeures ?: 0}h"
            
            val isPublished = parcours.isPublished ?: false
            textViewStatut.text = if (isPublished) "✅ Publié" else "📝 Brouillon"
            textViewStatut.setTextColor(
                itemView.context.getColor(
                    if (isPublished) R.color.success else R.color.warning
                )
            )
            
            cardParcours.setOnClickListener {
                onParcoursClick(parcours)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ParcoursResponse>() {
        override fun areItemsTheSame(oldItem: ParcoursResponse, newItem: ParcoursResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ParcoursResponse, newItem: ParcoursResponse): Boolean {
            return oldItem == newItem
        }
    }
}