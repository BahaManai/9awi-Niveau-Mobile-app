package com.example.kawi_niveau_mobile_app.ui.formateur.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.data.dto.CoursParNiveauDto

class NiveauxStatsAdapter : ListAdapter<CoursParNiveauDto, NiveauxStatsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_niveau_stats, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewNiveau: TextView = itemView.findViewById(R.id.textViewNiveau)
        private val textViewNombre: TextView = itemView.findViewById(R.id.textViewNombre)
        private val textViewPourcentage: TextView = itemView.findViewById(R.id.textViewPourcentage)

        fun bind(item: CoursParNiveauDto) {
            val niveau = item.niveau ?: "DEBUTANT"
            textViewNiveau.text = when(niveau) {
                "DEBUTANT" -> "🟢 Débutant"
                "INTERMEDIAIRE" -> "🟡 Intermédiaire"
                "AVANCE" -> "🔴 Avancé"
                else -> niveau
            }
            textViewNombre.text = "${item.nombre} cours"
            textViewPourcentage.text = "${String.format("%.1f", item.pourcentage)}%"
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CoursParNiveauDto>() {
        override fun areItemsTheSame(oldItem: CoursParNiveauDto, newItem: CoursParNiveauDto): Boolean {
            return oldItem.niveau == newItem.niveau
        }

        override fun areContentsTheSame(oldItem: CoursParNiveauDto, newItem: CoursParNiveauDto): Boolean {
            return oldItem == newItem
        }
    }
}