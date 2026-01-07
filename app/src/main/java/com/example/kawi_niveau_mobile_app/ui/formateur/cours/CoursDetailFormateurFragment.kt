package com.example.kawi_niveau_mobile_app.ui.formateur.cours

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.databinding.FragmentCoursDetailFormateurBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoursDetailFormateurFragment : Fragment() {

    private var _binding: FragmentCoursDetailFormateurBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CoursDetailFormateurViewModel by viewModels()
    private lateinit var moduleAdapter: ModuleReadOnlyAdapter
    private var coursId: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursDetailFormateurBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coursId = arguments?.getLong("coursId") ?: 0L

        setupToolbar()
        setupRecyclerView()
        observeViewModel()

        // Charger les données
        viewModel.loadCoursDetail(coursId)
        viewModel.loadCoursStats(coursId)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        moduleAdapter = ModuleReadOnlyAdapter()
        binding.recyclerViewModules.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moduleAdapter
        }
    }

    private fun observeViewModel() {
        // Observer le cours
        viewModel.cours.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val cours = result.data

                    binding.textViewTitre.text = cours.titre
                    binding.textViewDescription.text = cours.description
                    binding.textViewCategorie.text = cours.categorie ?: "Aucune catégorie"
                    binding.textViewNiveau.text = "🟢 ${cours.niveauDifficulteDisplay ?: "Débutant"}"
                    binding.textViewFormateur.text = cours.formateurNom
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Erreur: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Observer les statistiques
        viewModel.coursStats.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBarStats.visibility = View.VISIBLE
                    binding.cardViewStats.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBarStats.visibility = View.GONE
                    binding.cardViewStats.visibility = View.VISIBLE
                    
                    val stats = result.data
                    binding.textViewTotalInscrits.text = stats.totalInscrits.toString()
                    binding.textViewProgressionMoyenne.text = "${String.format("%.1f", stats.progressionMoyenne)}%"
                    binding.textViewTauxReussite.text = "${String.format("%.1f", stats.tauxReussite)}%"
                    binding.textViewCertificats.text = stats.nombreCertificats.toString()
                }
                is Resource.Error -> {
                    binding.progressBarStats.visibility = View.GONE
                    binding.cardViewStats.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Impossible de charger les statistiques",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Observer les modules
        viewModel.modules.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBarModules.visibility = View.VISIBLE
                    binding.recyclerViewModules.visibility = View.GONE
                    binding.textViewNoModules.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBarModules.visibility = View.GONE
                    
                    if (result.data.isEmpty()) {
                        binding.recyclerViewModules.visibility = View.GONE
                        binding.textViewNoModules.visibility = View.VISIBLE
                    } else {
                        binding.recyclerViewModules.visibility = View.VISIBLE
                        binding.textViewNoModules.visibility = View.GONE
                        moduleAdapter.submitList(result.data)
                    }
                }
                is Resource.Error -> {
                    binding.progressBarModules.visibility = View.GONE
                    binding.recyclerViewModules.visibility = View.GONE
                    binding.textViewNoModules.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}