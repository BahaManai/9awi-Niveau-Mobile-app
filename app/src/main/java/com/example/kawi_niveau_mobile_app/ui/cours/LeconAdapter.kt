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
import com.example.kawi_niveau_mobile_app.databinding.ItemLeconCardBinding

class LeconAdapter(
    private val onLeconClick: (Long) -> Unit,
    private val onToggleCompletion: (Long, Boolean) -> Unit
) : ListAdapter<LeconWithCompletion, LeconAdapter.LeconViewHolder>(LeconDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeconViewHolder {
        val binding = ItemLeconCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LeconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeconViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LeconViewHolder(
        private val binding: ItemLeconCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LeconWithCompletion) {
            val lecon = item.lecon

            // Numéro et titre
            binding.textViewLeconOrdre.text = (lecon.ordre ?: (adapterPosition + 1)).toString()
            binding.textViewLeconTitre.text = lecon.titre

            // Type de contenu avec icône
            binding.textViewTypeContenu.text = when (lecon.typeContenu) {
                "TEXTE" -> "📝 Texte"
                "PDF" -> "📄 PDF"
                "IMAGE" -> "🖼️ Image"
                "VIDEO" -> "🎥 Vidéo"
                else -> lecon.typeContenu
            }

            // Durée
            if (lecon.duree != null && lecon.duree > 0) {
                binding.textViewDuree.visibility = View.VISIBLE
                binding.textViewDuree.text = "${lecon.duree} min"
            } else {
                binding.textViewDuree.visibility = View.GONE
            }

            // Afficher le contenu selon le type
            when (lecon.typeContenu) {
                "TEXTE" -> {
                    binding.layoutTexte.visibility = View.VISIBLE
                    binding.layoutPdf.visibility = View.GONE
                    binding.layoutImage.visibility = View.GONE
                    binding.layoutVideo.visibility = View.GONE

                    binding.textViewContenuTexte.text = lecon.contenuTexte ?: "Aucun contenu"
                }
                "PDF" -> {
                    binding.layoutTexte.visibility = View.GONE
                    binding.layoutPdf.visibility = View.VISIBLE
                    binding.layoutImage.visibility = View.GONE
                    binding.layoutVideo.visibility = View.GONE
                }
                "IMAGE" -> {
                    binding.layoutTexte.visibility = View.GONE
                    binding.layoutPdf.visibility = View.GONE
                    binding.layoutImage.visibility = View.VISIBLE
                    binding.layoutVideo.visibility = View.GONE

                    if (!lecon.fichierUrl.isNullOrEmpty()) {
                        val imageUrl = "${BuildConfig.API_BASE_URL}api/files/lecons/${lecon.fichierUrl}"
                        Glide.with(binding.root.context)
                            .load(imageUrl)
                            .placeholder(R.drawable.cours_placeholder_gradient)
                            .error(R.drawable.cours_placeholder_gradient)
                            .centerCrop()
                            .into(binding.imageViewLecon)
                    }
                }
                "VIDEO" -> {
                    binding.layoutTexte.visibility = View.GONE
                    binding.layoutPdf.visibility = View.GONE
                    binding.layoutImage.visibility = View.GONE
                    binding.layoutVideo.visibility = View.VISIBLE

                    binding.textViewVideoUrl.text = "Vidéo disponible"
                }
            }

            // Bouton de complétion
            if (item.completed) {
                binding.buttonToggleCompletion.text = "✓ Leçon complétée"
                binding.buttonToggleCompletion.setBackgroundColor(
                    binding.root.context.getColor(R.color.accent_green)
                )
            } else {
                binding.buttonToggleCompletion.text = "Marquer comme complétée"
                binding.buttonToggleCompletion.setBackgroundColor(
                    binding.root.context.getColor(R.color.primary_blue)
                )
            }

            binding.buttonToggleCompletion.setOnClickListener {
                onToggleCompletion(lecon.id, item.completed)
            }

            binding.cardViewLecon.setOnClickListener {
                onLeconClick(lecon.id)
            }
        }
    }

    class LeconDiffCallback : DiffUtil.ItemCallback<LeconWithCompletion>() {
        override fun areItemsTheSame(
            oldItem: LeconWithCompletion,
            newItem: LeconWithCompletion
        ): Boolean {
            return oldItem.lecon.id == newItem.lecon.id
        }

        override fun areContentsTheSame(
            oldItem: LeconWithCompletion,
            newItem: LeconWithCompletion
        ): Boolean {
            return oldItem == newItem
        }
    }
}
