package com.example.kawi_niveau_mobile_app.ui.home.accueil

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kawi_niveau_mobile_app.databinding.ItemBadgeBinding
import com.example.kawi_niveau_mobile_app.data.network.dto.BadgeDto

class BadgeAdapter(
    private val onBadgeClick: (BadgeDto) -> Unit = {}
) : ListAdapter<BadgeDto, BadgeAdapter.BadgeViewHolder>(BadgeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
        val binding = ItemBadgeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BadgeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BadgeViewHolder(
        private val binding: ItemBadgeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(badge: BadgeDto) {
            binding.textViewBadgeName.text = badge.name
            binding.textViewBadgeDescription.text = badge.description
            
            // Afficher une icône selon le type de badge
            val icon = when {
                badge.criteriaType.contains("QUIZ") -> "🎯"
                badge.criteriaType.contains("COURSE") || badge.criteriaType.contains("COURS") -> "📚"
                badge.criteriaType.contains("LEVEL") -> "⭐"
                badge.criteriaType.contains("XP") -> "💎"
                badge.criteriaType.contains("PERFECT") -> "🏆"
                else -> "🎖️"
            }
            binding.textViewBadgeIcon.text = icon
            
            // Indicateur "nouveau"
            if (badge.isNew) {
                binding.badgeNewIndicator.visibility = android.view.View.VISIBLE
            } else {
                binding.badgeNewIndicator.visibility = android.view.View.GONE
            }
            
            binding.root.setOnClickListener {
                onBadgeClick(badge)
            }
        }
    }

    class BadgeDiffCallback : DiffUtil.ItemCallback<BadgeDto>() {
        override fun areItemsTheSame(oldItem: BadgeDto, newItem: BadgeDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BadgeDto, newItem: BadgeDto): Boolean {
            return oldItem == newItem
        }
    }
}
