package com.example.kawi_niveau_mobile_app.ui.formateur.cours

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.databinding.FragmentCoursListBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoursListFragment : Fragment() {

    private var _binding: FragmentCoursListBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: CoursViewModel by viewModels()
    private lateinit var adapter: CoursAdapter

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
        setupObservers()
        setupListeners()
        
        viewModel.loadMesCours()
    }

    private fun setupRecyclerView() {
        adapter = CoursAdapter(
            onItemClick = { cours ->
                val action = CoursListFragmentDirections
                    .actionNavFormateurCoursToCoursDetailFragment(cours.id)
                findNavController().navigate(action)
            },
            onEditClick = { cours ->
                val action = CoursListFragmentDirections
                    .actionNavFormateurCoursToCoursFormFragment(cours.id)
                findNavController().navigate(action)
            },
            onArchiveClick = { cours ->
                showArchiveDialog(cours.id, cours.archived)
            }
        )
        
        binding.recyclerViewCours.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@CoursListFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.cours.observe(viewLifecycleOwner) { coursList ->
            adapter.submitList(coursList)
            binding.emptyView.visibility = if (coursList.isEmpty()) View.VISIBLE else View.GONE
            binding.recyclerViewCours.visibility = if (coursList.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.clearMessages()
            }
        }

        viewModel.success.observe(viewLifecycleOwner) { success ->
            success?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                viewModel.clearMessages()
            }
        }
    }

    private fun setupListeners() {
        binding.fabAddCours.setOnClickListener {
            navigateToForm()
        }
        
        binding.buttonCreateFirst.setOnClickListener {
            navigateToForm()
        }
        
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadMesCours()
            binding.swipeRefresh.isRefreshing = false
        }
    }
    
    private fun navigateToForm() {
        val action = CoursListFragmentDirections
            .actionNavFormateurCoursToCoursFormFragment(-1L)
        findNavController().navigate(action)
    }

    private fun showArchiveDialog(coursId: Long, isArchived: Boolean) {
        val title = if (isArchived) "Réactiver le cours" else "Archiver le cours"
        val message = if (isArchived) {
            "Voulez-vous réactiver ce cours ?"
        } else {
            "Voulez-vous archiver ce cours ? Il ne sera plus visible par les étudiants."
        }
        
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Oui") { _, _ ->
                if (isArchived) {
                    viewModel.unarchiveCours(coursId)
                } else {
                    viewModel.archiveCours(coursId)
                }
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
