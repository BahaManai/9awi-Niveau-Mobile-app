package com.example.kawi_niveau_mobile_app.ui.cours

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kawi_niveau_mobile_app.BuildConfig
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.databinding.ItemCoursCardBinding

class CoursAdapter(
    private val onCoursClick: (Long) -> Unit,
    private val onEnrollClick: (Long) -> Unit
) : ListAdapter<CoursWithEnrollment, CoursAdapter.CoursViewHolder>(CoursDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursViewHolder {
        val binding = ItemCoursCardBinding.inflate(
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
        private val binding: ItemCoursCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CoursWithEnrollment) {
            val cours = item.cours
            val enrollment = item.enrollment

            // Titre et description
            binding.textViewTitre.text = cours.titre
            binding.textViewDescription.text = cours.description

            // Catégorie
            binding.textViewCategorie.text = cours.categorie ?: "Cours"

            // Formateur
            binding.textViewFormateur.text = cours.formateurNom

            // Image du cours
            if (!cours.thumbnailUrl.isNullOrEmpty()) {
                val imageUrl = "${BuildConfig.API_BASE_URL}images/cours/${cours.thumbnailUrl}"
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.cours_placeholder_gradient)
                    .error(R.drawable.cours_placeholder_gradient)
                    .centerCrop()
                    .into(binding.imageViewCoursThumbnail)
            } else {
                binding.imageViewCoursThumbnail.setBackgroundResource(R.drawable.cours_placeholder_gradient)
            }

            // Badges et progression
            if (item.isEnrolled && enrollment != null) {
                // Badge inscrit
                binding.textViewEnrolledBadge.visibility = View.VISIBLE
                binding.textViewNewBadge.visibility = View.GONE

                // Progression
                binding.layoutProgress.visibility = View.VISIBLE
                binding.textViewProgress.text = "${enrollment.progress.toInt()}%"
                binding.progressBarCours.progress = enrollment.progress.toInt()
                binding.textViewLecons.text = "${enrollment.completedLecons} / ${enrollment.totalLecons} leçons"

                // Bouton action
                binding.buttonAction.text = "Continuer"
                binding.buttonEnroll.visibility = View.GONE
            } else {
                // Badge nouveau
                binding.textViewEnrolledBadge.visibility = View.GONE
                binding.textViewNewBadge.visibility = View.VISIBLE

                // Pas de progression
                binding.layoutProgress.visibility = View.GONE

                // Boutons
                binding.buttonAction.text = "Voir détails"
                binding.buttonEnroll.visibility = View.VISIBLE
            }

            // Click listeners
            binding.cardViewCours.setOnClickListener {
                onCoursClick(cours.id)
            }

            binding.buttonAction.setOnClickListener {
                onCoursClick(cours.id)
            }

            binding.buttonEnroll.setOnClickListener {
                onEnrollClick(cours.id)
            }
        }
    }

    class CoursDiffCallback : DiffUtil.ItemCallback<CoursWithEnrollment>() {
        override fun areItemsTheSame(
            oldItem: CoursWithEnrollment,
            newItem: CoursWithEnrollment
        ): Boolean {
            return oldItem.cours.id == newItem.cours.id
        }

        override fun areContentsTheSame(
            oldItem: CoursWithEnrollment,
            newItem: CoursWithEnrollment
        ): Boolean {
            return oldItem == newItem
        }
    }
}
