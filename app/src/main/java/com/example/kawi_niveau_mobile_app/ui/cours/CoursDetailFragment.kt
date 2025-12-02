package com.example.kawi_niveau_mobile_app.ui.cours

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kawi_niveau_mobile_app.BuildConfig
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.databinding.FragmentCoursDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoursDetailFragment : Fragment() {

    private var _binding: FragmentCoursDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CoursDetailViewModel by viewModels()
    private lateinit var moduleAdapter: ModuleAdapter
    private var coursId: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Récupérer l'argument coursId
        coursId = arguments?.getLong("coursId") ?: 0L

        setupToolbar()
        setupRecyclerView()
        observeViewModel()

        // Charger les données
        viewModel.loadCoursDetail(coursId)
    }

    private fun setupToolbar() {
        // TODO: Uncomment when toolbar is added to layout
        // binding.toolbar.setNavigationOnClickListener {
        //     findNavController().navigateUp()
        // }
    }

    private fun setupRecyclerView() {
        moduleAdapter = ModuleAdapter { moduleId ->
            // Navigation vers le détail du module
            val bundle = Bundle().apply {
                putLong("moduleId", moduleId)
            }
            findNavController().navigate(R.id.moduleDetailFragment, bundle)
        }

        // TODO: Uncomment when recyclerView is added to layout
        // binding.recyclerViewModules.apply {
        //     layoutManager = LinearLayoutManager(requireContext())
        //     adapter = moduleAdapter
        // }
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
                    binding.textViewCategorie.text = cours.categorie ?: "Cours"
                    binding.textViewFormateur.text = cours.formateurNom

                    // Image
                    // TODO: Uncomment when imageView is added to layout
                    // if (!cours.thumbnailUrl.isNullOrEmpty()) {
                    //     val imageUrl = "${BuildConfig.API_BASE_URL}images/cours/${cours.thumbnailUrl}"
                    //     Glide.with(this)
                    //         .load(imageUrl)
                    //         .placeholder(R.drawable.cours_placeholder_gradient)
                    //         .error(R.drawable.cours_placeholder_gradient)
                    //         .centerCrop()
                    //         .into(binding.imageViewCoursHeader)
                    // }
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

        // Observer l'enrollment
        // TODO: Uncomment when enrollment views are added to layout
        // viewModel.enrollment.observe(viewLifecycleOwner) { result ->
        //     when (result) {
        //         is Resource.Success -> {
        //             val enrollment = result.data
        //             if (enrollment != null) {
        //                 // Inscrit
        //                 binding.cardViewProgress.visibility = View.VISIBLE
        //                 binding.buttonEnroll.visibility = View.GONE
        //
        //                 binding.textViewProgress.text = "${enrollment.progress.toInt()}%"
        //                 binding.progressBarCours.progress = enrollment.progress.toInt()
        //                 binding.textViewLecons.text = "${enrollment.completedLecons} / ${enrollment.totalLecons} leçons complétées"
        //             } else {
        //                 // Pas inscrit
        //                 binding.cardViewProgress.visibility = View.GONE
        //                 binding.buttonEnroll.visibility = View.VISIBLE
        //             }
        //         }
        //         else -> {}
        //     }
        // }

        // Observer les modules
        // TODO: Uncomment when module views are added to layout
        // viewModel.modules.observe(viewLifecycleOwner) { result ->
        //     when (result) {
        //         is Resource.Loading -> {
        //             binding.progressBarModules.visibility = View.VISIBLE
        //             binding.recyclerViewModules.visibility = View.GONE
        //             binding.textViewNoModules.visibility = View.GONE
        //         }
        //         is Resource.Success -> {
        //             binding.progressBarModules.visibility = View.GONE
        //
        //             if (result.data.isEmpty()) {
        //                 binding.recyclerViewModules.visibility = View.GONE
        //                 binding.textViewNoModules.visibility = View.VISIBLE
        //             } else {
        //                 binding.recyclerViewModules.visibility = View.VISIBLE
        //                 binding.textViewNoModules.visibility = View.GONE
        //                 moduleAdapter.submitList(result.data)
        //             }
        //         }
        //         is Resource.Error -> {
        //             binding.progressBarModules.visibility = View.GONE
        //             binding.recyclerViewModules.visibility = View.GONE
        //             binding.textViewNoModules.visibility = View.VISIBLE
        //         }
        //     }
        // }

        // Observer le résultat d'inscription
        // TODO: Uncomment when enrollment button is added to layout
        // viewModel.enrollmentResult.observe(viewLifecycleOwner) { result ->
        //     when (result) {
        //         is Resource.Loading -> {
        //             binding.buttonEnroll.isEnabled = false
        //             binding.buttonEnroll.text = "Inscription en cours..."
        //         }
        //         is Resource.Success -> {
        //             binding.buttonEnroll.isEnabled = true
        //             binding.buttonEnroll.text = "S'inscrire à ce cours"
        //             Toast.makeText(
        //                 requireContext(),
        //                 "🎉 Inscription réussie !",
        //                 Toast.LENGTH_SHORT
        //             ).show()
        //             viewModel.resetEnrollmentResult()
        //         }
        //         is Resource.Error -> {
        //             binding.buttonEnroll.isEnabled = true
        //             binding.buttonEnroll.text = "S'inscrire à ce cours"
        //             Toast.makeText(
        //                 requireContext(),
        //                 "Erreur: ${result.message}",
        //                 Toast.LENGTH_SHORT
        //             ).show()
        //             viewModel.resetEnrollmentResult()
        //         }
        //         null -> {
        //             binding.buttonEnroll.isEnabled = true
        //         }
        //     }
        // }

        // Bouton inscription
        // TODO: Uncomment when enrollment button is added to layout
        // binding.buttonEnroll.setOnClickListener {
        //     viewModel.enrollInCourse(coursId)
        // }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
