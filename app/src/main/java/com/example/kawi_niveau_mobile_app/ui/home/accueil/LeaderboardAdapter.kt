package com.example.kawi_niveau_mobile_app.ui.home.accueil

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.databinding.ItemLeaderboardBinding
import com.example.kawi_niveau_mobile_app.data.network.dto.LeaderboardEntryDto

class LeaderboardAdapter : ListAdapter<LeaderboardEntryDto, LeaderboardAdapter.LeaderboardViewHolder>(LeaderboardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val binding = ItemLeaderboardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LeaderboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LeaderboardViewHolder(
        private val binding: ItemLeaderboardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: LeaderboardEntryDto) {
            // Rang avec médaille pour le top 3
            binding.textViewRank.text = when (entry.rank) {
                1 -> "🥇"
                2 -> "🥈"
                3 -> "🥉"
                else -> "#${entry.rank}"
            }
            
            binding.textViewName.text = entry.name
            binding.textViewLevel.text = entry.levelName
            binding.textViewPoints.text = "${entry.totalPoints} XP"
            binding.textViewBadges.text = "🏆 ${entry.badgesCount}"
            
            // Mettre en évidence l'utilisateur actuel
            if (entry.isCurrentUser) {
                binding.root.setBackgroundColor(
                    binding.root.context.getColor(R.color.primary_blue_light)
                )
                binding.textViewName.setTextColor(
                    binding.root.context.getColor(R.color.primary_blue)
                )
            } else {
                binding.root.setBackgroundColor(
                    binding.root.context.getColor(android.R.color.transparent)
                )
                binding.textViewName.setTextColor(
                    binding.root.context.getColor(android.R.color.black)
                )
            }
        }
    }

    class LeaderboardDiffCallback : DiffUtil.ItemCallback<LeaderboardEntryDto>() {
        override fun areItemsTheSame(oldItem: LeaderboardEntryDto, newItem: LeaderboardEntryDto): Boolean {
            return oldItem.rank == newItem.rank
        }

        override fun areContentsTheSame(oldItem: LeaderboardEntryDto, newItem: LeaderboardEntryDto): Boolean {
            return oldItem == newItem
        }
    }
}
