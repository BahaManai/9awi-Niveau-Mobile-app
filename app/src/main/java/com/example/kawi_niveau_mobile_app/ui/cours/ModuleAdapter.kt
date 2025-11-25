package com.example.kawi_niveau_mobile_app.ui.cours

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kawi_niveau_mobile_app.data.responses.ModuleProgressResponse
import com.example.kawi_niveau_mobile_app.databinding.ItemModuleCardBinding

class ModuleAdapter(
    private val onModuleClick: (Long) -> Unit
) : ListAdapter<ModuleProgressResponse, ModuleAdapter.ModuleViewHolder>(ModuleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val binding = ItemModuleCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ModuleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ModuleViewHolder(
        private val binding: ItemModuleCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(module: ModuleProgressResponse) {
            // Numéro et titre
            binding.textViewModuleOrdre.text = module.ordre.toString()
            binding.textViewModuleTitre.text = module.titre

            // Leçons
            binding.textViewModuleLecons.text = "${module.leconsCompletees} / ${module.totalLecons} leçons"

            // Progression (visible si au moins une leçon)
            if (module.totalLecons > 0) {
                binding.layoutModuleProgress.visibility = View.VISIBLE
                binding.progressBarModule.progress = module.progressionLecons.toInt()
                binding.textViewModuleProgress.text = "${module.progressionLecons.toInt()}%"
            } else {
                binding.layoutModuleProgress.visibility = View.GONE
            }

            // Quiz badge
            if (module.hasQuiz) {
                binding.textViewQuizBadge.visibility = View.VISIBLE
                if (module.quizPassed) {
                    binding.textViewQuizBadge.text = "✅ Quiz réussi"
                } else {
                    binding.textViewQuizBadge.text = "📝 Quiz disponible"
                }
            } else {
                binding.textViewQuizBadge.visibility = View.GONE
            }

            // Click listener
            binding.cardViewModule.setOnClickListener {
                onModuleClick(module.id)
            }
        }
    }

    class ModuleDiffCallback : DiffUtil.ItemCallback<ModuleProgressResponse>() {
        override fun areItemsTheSame(
            oldItem: ModuleProgressResponse,
            newItem: ModuleProgressResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ModuleProgressResponse,
            newItem: ModuleProgressResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
}
