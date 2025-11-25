package com.example.kawi_niveau_mobile_app.ui.cours

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.databinding.FragmentCoursListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoursListFragment : Fragment() {

    private var _binding: FragmentCoursListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CoursListViewModel by viewModels()
    private lateinit var coursAdapter: CoursAdapter

    private var allCours: List<CoursWithEnrollment> = emptyList()
    private var filteredCours: List<CoursWithEnrollment> = emptyList()
    private var categories: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchAndFilters()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        coursAdapter = CoursAdapter(
            onCoursClick = { coursId ->
                // Navigation vers le détail du cours
                val bundle = Bundle().apply {
                    putLong("coursId", coursId)
                }
                findNavController().navigate(R.id.coursDetailFragment, bundle)
            },
            onEnrollClick = { coursId ->
                // Inscription au cours
                viewModel.enrollInCourse(coursId)
            }
        )

        binding.recyclerViewCours.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = coursAdapter
        }
    }

    private fun setupSearchAndFilters() {
        // Recherche
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                applyFilters()
            }
        })
    }

    private fun observeViewModel() {
        // Observer la liste des cours
        viewModel.coursList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewCours.visibility = View.GONE
                    binding.layoutEmptyState.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE

                    allCours = result.data
                    filteredCours = result.data

                    if (result.data.isEmpty()) {
                        binding.recyclerViewCours.visibility = View.GONE
                        binding.layoutEmptyState.visibility = View.VISIBLE
                    } else {
                        binding.recyclerViewCours.visibility = View.VISIBLE
                        binding.layoutEmptyState.visibility = View.GONE
                        coursAdapter.submitList(result.data)

                        // Extraire les catégories
                        extractCategories(result.data)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewCours.visibility = View.GONE
                    binding.layoutEmptyState.visibility = View.VISIBLE
                    Toast.makeText(
                        requireContext(),
                        "Erreur: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Observer les statistiques
        viewModel.userStats.observe(viewLifecycleOwner) { stats ->
            binding.textViewEnrolledCount.text = stats.enrolledCount.toString()
            binding.textViewCompletedCount.text = stats.completedCount.toString()
            binding.textViewOverallProgress.text = "${stats.overallProgress}%"
            binding.textViewUserLevel.text = "Niveau ${stats.userLevel}"
            binding.textViewTotalPoints.text = "${stats.totalPoints} pts"
        }

        // Observer le résultat d'inscription
        viewModel.enrollmentResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    // Optionnel: afficher un loading
                }
                is Resource.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "🎉 Inscription réussie ! Vous avez gagné 50 points !",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.resetEnrollmentResult()
                }
                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Erreur: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.resetEnrollmentResult()
                }
                null -> {
                    // Pas de résultat
                }
            }
        }
    }

    private fun extractCategories(coursList: List<CoursWithEnrollment>) {
        // Méthode conservée pour compatibilité mais ne fait plus rien
        // Le filtre catégorie a été supprimé de l'UI
    }

    private fun applyFilters() {
        val searchQuery = binding.editTextSearch.text.toString().lowercase()

        filteredCours = allCours.filter { coursWithEnrollment ->
            val cours = coursWithEnrollment.cours

            // Filtre par recherche uniquement
            searchQuery.isEmpty() ||
                    cours.titre.lowercase().contains(searchQuery) ||
                    cours.description.lowercase().contains(searchQuery)
        }

        coursAdapter.submitList(filteredCours)

        // Afficher empty state si aucun résultat
        if (filteredCours.isEmpty() && allCours.isNotEmpty()) {
            binding.recyclerViewCours.visibility = View.GONE
            binding.layoutEmptyState.visibility = View.VISIBLE
        } else {
            binding.recyclerViewCours.visibility = View.VISIBLE
            binding.layoutEmptyState.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
