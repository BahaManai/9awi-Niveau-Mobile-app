package com.example.kawi_niveau_mobile_app.ui.formateur.cours

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kawi_niveau_mobile_app.BuildConfig
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.data.responses.CoursResponse
import com.example.kawi_niveau_mobile_app.databinding.ItemCoursBinding
import java.text.SimpleDateFormat
import java.util.*

class CoursAdapter(
    private val onItemClick: (CoursResponse) -> Unit,
    private val onEditClick: (CoursResponse) -> Unit,
    private val onArchiveClick: (CoursResponse) -> Unit
) : ListAdapter<CoursResponse, CoursAdapter.CoursViewHolder>(CoursDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursViewHolder {
        val binding = ItemCoursBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CoursViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoursViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CoursViewHolder(
        private val binding: ItemCoursBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cours: CoursResponse) {
            binding.apply {
                textViewTitre.text = cours.titre
                textViewDescription.text = cours.description ?: "Aucune description"
                textViewCategorie.text = cours.categorie ?: "Cours"
                textViewDate.text = formatDate(cours.createdAt)
                
                chipArchived.visibility = if (cours.archived) android.view.View.VISIBLE else android.view.View.GONE
                
                cours.thumbnailUrl?.let { filename ->
                    val imageUrl = "${BuildConfig.API_BASE_URL}images/cours/$filename"
                    Glide.with(itemView.context)
                        .load(imageUrl)
                        .centerCrop()
                        .into(imageViewThumbnail)
                }
                
                root.setOnClickListener { onItemClick(cours) }
                buttonEdit.setOnClickListener { onEditClick(cours) }
                buttonArchive.apply {
                    text = if (cours.archived) "Réactiver" else "Archiver"
                    setOnClickListener { onArchiveClick(cours) }
                }
            }
        }

        private fun formatDate(timestamp: Long): String {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.FRENCH)
            return sdf.format(Date(timestamp))
        }
    }

    class CoursDiffCallback : DiffUtil.ItemCallback<CoursResponse>() {
        override fun areItemsTheSame(oldItem: CoursResponse, newItem: CoursResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CoursResponse, newItem: CoursResponse): Boolean {
            return oldItem == newItem
        }
    }
}
