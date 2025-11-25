package com.example.kawi_niveau_mobile_app.ui.cours

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
import com.example.kawi_niveau_mobile_app.databinding.FragmentModuleDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModuleDetailFragment : Fragment() {

    private var _binding: FragmentModuleDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ModuleDetailViewModel by viewModels()
    private lateinit var leconAdapter: LeconAdapter
    private var moduleId: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModuleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Récupérer l'argument moduleId
        moduleId = arguments?.getLong("moduleId") ?: 0L

        setupToolbar()
        setupRecyclerView()
        observeViewModel()

        // Charger les données
        viewModel.loadModuleDetail(moduleId)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        leconAdapter = LeconAdapter(
            onLeconClick = { leconId ->
                // Optionnel : Navigation vers détail de la leçon
            },
            onToggleCompletion = { leconId, currentlyCompleted ->
                viewModel.toggleLeconCompletion(leconId, currentlyCompleted)
            }
        )

        binding.recyclerViewLecons.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = leconAdapter
        }
    }

    private fun observeViewModel() {
        // Observer le module
        viewModel.module.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val module = result.data

                    binding.textViewModuleTitre.text = module.titre
                    binding.textViewModuleContenu.text = module.contenu ?: "Aucune description"
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

        // Observer les leçons
        viewModel.lecons.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBarLecons.visibility = View.VISIBLE
                    binding.recyclerViewLecons.visibility = View.GONE
                    binding.layoutEmptyState.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBarLecons.visibility = View.GONE

                    if (result.data.isEmpty()) {
                        binding.recyclerViewLecons.visibility = View.GONE
                        binding.layoutEmptyState.visibility = View.VISIBLE
                    } else {
                        binding.recyclerViewLecons.visibility = View.VISIBLE
                        binding.layoutEmptyState.visibility = View.GONE
                        leconAdapter.submitList(result.data)
                    }
                }
                is Resource.Error -> {
                    binding.progressBarLecons.visibility = View.GONE
                    binding.recyclerViewLecons.visibility = View.GONE
                    binding.layoutEmptyState.visibility = View.VISIBLE
                    Toast.makeText(
                        requireContext(),
                        "Erreur: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Observer le résultat de complétion
        viewModel.completionResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    // Optionnel : afficher un loading
                }
                is Resource.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "✓ Mis à jour !",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.resetCompletionResult()
                }
                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Erreur: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.resetCompletionResult()
                }
                null -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
