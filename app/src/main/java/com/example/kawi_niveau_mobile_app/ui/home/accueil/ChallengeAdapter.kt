package com.example.kawi_niveau_mobile_app.ui.home.accueil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kawi_niveau_mobile_app.databinding.ItemChallengeBinding
import com.example.kawi_niveau_mobile_app.data.network.dto.ChallengeDto

class ChallengeAdapter(
    private val onChallengeClick: (ChallengeDto) -> Unit = {}
) : ListAdapter<ChallengeDto, ChallengeAdapter.ChallengeViewHolder>(ChallengeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val binding = ItemChallengeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChallengeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChallengeViewHolder(
        private val binding: ItemChallengeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(challenge: ChallengeDto) {
            binding.textViewChallengeName.text = challenge.name
            binding.textViewChallengeDescription.text = challenge.description
            
            // Progression
            binding.textViewChallengeProgress.text = 
                "${challenge.currentProgress} / ${challenge.targetValue}"
            binding.progressBarChallenge.progress = challenge.progressPercent.toInt()
            
            // Récompense XP
            binding.textViewChallengeReward.text = "+${challenge.xpReward} XP"
            
            // Temps restant
            if (challenge.timeRemaining != null) {
                binding.textViewChallengeTimeRemaining.visibility = View.VISIBLE
                binding.textViewChallengeTimeRemaining.text = "⏱️ ${challenge.timeRemaining}"
            } else {
                binding.textViewChallengeTimeRemaining.visibility = View.GONE
            }
            
            // État du défi
            if (challenge.isCompleted) {
                binding.textViewChallengeStatus.visibility = View.VISIBLE
                binding.textViewChallengeStatus.text = "✓ Terminé"
                binding.textViewChallengeStatus.setBackgroundColor(
                    binding.root.context.getColor(android.R.color.holo_green_light)
                )
            } else if (challenge.isNew) {
                binding.textViewChallengeStatus.visibility = View.VISIBLE
                binding.textViewChallengeStatus.text = "🆕 Nouveau"
                binding.textViewChallengeStatus.setBackgroundColor(
                    binding.root.context.getColor(android.R.color.holo_orange_light)
                )
            } else {
                binding.textViewChallengeStatus.visibility = View.GONE
            }
            
            binding.root.setOnClickListener {
                onChallengeClick(challenge)
            }
        }
    }

    class ChallengeDiffCallback : DiffUtil.ItemCallback<ChallengeDto>() {
        override fun areItemsTheSame(oldItem: ChallengeDto, newItem: ChallengeDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChallengeDto, newItem: ChallengeDto): Boolean {
            return oldItem == newItem
        }
    }
}
