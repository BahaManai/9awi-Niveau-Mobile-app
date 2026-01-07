package com.example.kawi_niveau_mobile_app.ui.formateur.dashboard

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.data.dto.ParcoursResponse
import com.example.kawi_niveau_mobile_app.databinding.FragmentFormateurDashboardBinding
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FormateurDashboardFragment : Fragment() {

    private var _binding: FragmentFormateurDashboardBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FormateurDashboardViewModel by viewModels()
    
    private lateinit var niveauxStatsAdapter: NiveauxStatsAdapter
    private lateinit var parcoursAdapter: ParcoursAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormateurDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerViews()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        // Adapter pour les statistiques par niveau
        niveauxStatsAdapter = NiveauxStatsAdapter()
        binding.recyclerViewNiveauxStats.apply {
            adapter = niveauxStatsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        
        // Adapter pour les parcours
        parcoursAdapter = ParcoursAdapter { parcours ->
            showParcoursProgressionStats(parcours)
        }
        binding.recyclerViewParcours.apply {
            adapter = parcoursAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupClickListeners() {
        // Bouton supprimé - plus de gestion web depuis le dashboard
        // La gestion se fait maintenant via les alertes dans les détails de cours
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                updateUI(uiState)
            }
        }
    }

    private fun updateUI(uiState: FormateurDashboardUiState) {
        // Gestion du loading
        binding.progressBar.visibility = if (uiState.isLoading) View.VISIBLE else View.GONE
        
        // Gestion des erreurs
        if (uiState.error != null) {
            Toast.makeText(requireContext(), uiState.error, Toast.LENGTH_LONG).show()
        }
        
        // Mise à jour des statistiques du formateur
        uiState.formateurStats?.let { stats ->
            binding.textViewTotalCours.text = stats.totalCours.toString()
            binding.textViewCoursActifs.text = stats.coursActifs.toString()
            niveauxStatsAdapter.submitList(stats.coursParNiveau)
        }
        
        // Mise à jour de la liste des parcours
        parcoursAdapter.submitList(uiState.parcoursList)
        binding.textViewNoParcours.visibility = 
            if (uiState.parcoursList.isEmpty()) View.VISIBLE else View.GONE
        
        // Affichage des statistiques de progression si sélectionnées
        uiState.selectedParcoursStats?.let { stats ->
            showProgressionStatsDialog(stats)
            viewModel.clearSelectedParcoursStats()
        }
    }

    private fun showParcoursProgressionStats(parcours: ParcoursResponse) {
        viewModel.loadParcoursProgressionStats(parcours.id)
    }

    private fun showProgressionStatsDialog(stats: com.example.kawi_niveau_mobile_app.data.dto.ParcoursProgressionStatsResponse) {
        val dialog = Dialog(requireContext())
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_parcours_progression_stats, null)
        
        dialog.setContentView(dialogView)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        
        // Remplir les données
        dialogView.findViewById<TextView>(R.id.textViewParcoursTitre).text = stats.parcoursTitre
        dialogView.findViewById<TextView>(R.id.textViewTotalInscrits).text = stats.totalInscrits.toString()
        dialogView.findViewById<TextView>(R.id.textViewNombreTermines).text = stats.nombreTermines.toString()
        dialogView.findViewById<TextView>(R.id.textViewNombreEnCours).text = stats.nombreEnCours.toString()
        dialogView.findViewById<TextView>(R.id.textViewNombreCertificats).text = stats.nombreCertificats.toString()
        
        val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBarMoyenne)
        val textViewProgression = dialogView.findViewById<TextView>(R.id.textViewProgressionMoyenne)
        
        progressBar.progress = stats.progressionMoyenne.toInt()
        textViewProgression.text = "${String.format("%.1f", stats.progressionMoyenne)}%"
        
        dialogView.findViewById<MaterialButton>(R.id.buttonFermer).setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
