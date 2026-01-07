package com.example.kawi_niveau_mobile_app.ui.formateur.cours

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kawi_niveau_mobile_app.data.responses.ModuleResponse
import com.example.kawi_niveau_mobile_app.databinding.ItemModuleReadonlyBinding
import kotlin.random.Random

class ModuleReadOnlyAdapter : ListAdapter<ModuleResponse, ModuleReadOnlyAdapter.ModuleViewHolder>(ModuleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val binding = ItemModuleReadonlyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ModuleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ModuleViewHolder(
        private val binding: ItemModuleReadonlyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(module: ModuleResponse) {
            binding.textViewModuleTitre.text = module.titre
            binding.textViewModuleDescription.text = module.contenu ?: "Aucune description"
            
            // Pour le moment, simuler le nombre de leçons
            // TODO: Ajouter les leçons dans ModuleResponse ou faire un appel séparé
            val nombreLecons = Random.nextInt(3, 9)
            binding.textViewNombreLecons.text = if (nombreLecons <= 1) {
                "$nombreLecons leçon"
            } else {
                "$nombreLecons leçons"
            }
            
            // Afficher l'ordre du module
            binding.textViewOrdre.text = "Module ${module.ordre}"
        }
    }

    private class ModuleDiffCallback : DiffUtil.ItemCallback<ModuleResponse>() {
        override fun areItemsTheSame(oldItem: ModuleResponse, newItem: ModuleResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ModuleResponse, newItem: ModuleResponse): Boolean {
            return oldItem == newItem
        }
    }
}