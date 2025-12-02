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
                val bundle = Bundle().apply {
                    putLong("coursId", coursId)
                }
                findNavController().navigate(R.id.coursDetailFragment, bundle)
            },
            onEnrollClick = { coursId ->
                viewModel.enrollInCourse(coursId)
            }
        )

        binding.recyclerViewCours.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = coursAdapter
        }
    }

    private fun setupSearchAndFilters() {
    }

    private fun observeViewModel() {
        viewModel.coursList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewCours.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    allCours = result.data
                    filteredCours = result.data

                    if (result.data.isEmpty()) {
                        binding.recyclerViewCours.visibility = View.GONE
                    } else {
                        binding.recyclerViewCours.visibility = View.VISIBLE
                        coursAdapter.submitList(result.data)
                        extractCategories(result.data)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewCours.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Erreur: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.enrollmentResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {}
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
                null -> {}
            }
        }
    }

    private fun extractCategories(coursList: List<CoursWithEnrollment>) {
    }

    private fun applyFilters() {
        val searchQuery = ""

        filteredCours = allCours.filter { coursWithEnrollment ->
            val cours = coursWithEnrollment.cours
            searchQuery.isEmpty() ||
                    cours.titre.lowercase().contains(searchQuery) ||
                    cours.description.lowercase().contains(searchQuery)
        }

        coursAdapter.submitList(filteredCours)

        if (filteredCours.isEmpty() && allCours.isNotEmpty()) {
            binding.recyclerViewCours.visibility = View.GONE
        } else {
            binding.recyclerViewCours.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
