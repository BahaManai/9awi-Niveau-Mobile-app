package com.example.kawi_niveau_mobile_app.ui.formateur.cours

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.kawi_niveau_mobile_app.BuildConfig
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.databinding.FragmentFormateurCoursDetailBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class FormateurCoursDetailFragment : Fragment() {

    private var _binding: FragmentFormateurCoursDetailBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: CoursViewModel by viewModels()
    // Utilisation du nom généré correct : FormateurCoursDetailFragmentArgs
    private val args: FormateurCoursDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormateurCoursDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupObservers()
        viewModel.loadCoursById(args.coursId)
        
        binding.buttonEditContent?.setOnClickListener {
            Snackbar.make(binding.root, "Gestion des modules bientôt disponible", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setupObservers() {
        viewModel.coursDetail.observe(viewLifecycleOwner) { cours ->
            binding.apply {
                textViewTitre.text = cours.titre
                textViewDescription.text = cours.description ?: "Aucune description"
                textViewCategorie.text = cours.categorie ?: "Cours"
                textViewFormateur.text = "Par ${cours.formateurNom}"
                textViewDate.text = "Créé le ${formatDate(cours.createdAt)}"
                
                chipArchived.visibility = if (cours.archived) View.VISIBLE else View.GONE
                
                cours.thumbnailUrl?.let { filename ->
                    val imageUrl = "${BuildConfig.API_BASE_URL}images/cours/$filename"
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_course_placeholder)
                        .into(imageViewThumbnail)
                } ?: run {
                    imageViewThumbnail.setImageResource(R.drawable.ic_course_placeholder)
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.contentLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.clearMessages()
            }
        }
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH)
        return sdf.format(Date(timestamp))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
