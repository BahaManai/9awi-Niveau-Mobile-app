package com.example.kawi_niveau_mobile_app.ui.home.profil

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.kawi_niveau_mobile_app.BuildConfig
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        observeProfile()
        setupButtons()
    }

    private fun observeProfile() {
        viewModel.profile.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val profile = result.data
                    
                    // Nom complet
                    val fullName = "${profile.firstName ?: ""} ${profile.lastName ?: ""}".trim()
                    binding.textViewProfileName.text = if (fullName.isNotEmpty()) fullName else "Utilisateur"
                    
                    // Email
                    binding.textViewProfileEmail.text = profile.email
                    
                    // Email vérifié
                    if (profile.emailVerified) {
                        binding.textViewEmailVerified.text = "Email vérifié ✓"
                        binding.textViewEmailVerified.setTextColor(resources.getColor(android.R.color.holo_green_dark, null))
                    } else {
                        binding.textViewEmailVerified.text = "Email non vérifié"
                        binding.textViewEmailVerified.setTextColor(resources.getColor(android.R.color.holo_orange_dark, null))
                    }
                    
                    // Date de naissance
                    binding.textViewDateOfBirth.text = formatDateOfBirth(profile.dateOfBirth)
                    
                    // Numéro de téléphone
                    binding.textViewPhoneNumber.text = profile.phoneNumber ?: "Non renseigné"
                    
                    // Date d'inscription
                    binding.textViewCreatedAt.text = formatTimestamp(profile.createdAt)
                    
                    // Provider
                    binding.textViewProvider.text = when (profile.provider) {
                        "local" -> "Email"
                        "google" -> "Google"
                        else -> profile.provider
                    }
                    
                    // Domaine de spécialisation (seulement pour les formateurs)
                    if (profile.role == "FORMATEUR" && !profile.domaineSpecialisation.isNullOrEmpty()) {
                        binding.layoutDomaineSpecialisation.visibility = View.VISIBLE
                        binding.textViewDomaineSpecialisation.text = profile.domaineSpecialisation
                    } else {
                        binding.layoutDomaineSpecialisation.visibility = View.GONE
                    }
                    
                    // Photo de profil
                    loadProfileImage(profile.profileImage)
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
    }

    private fun loadProfileImage(filename: String?) {
        filename?.let {
            val imageUrl = "${BuildConfig.API_BASE_URL}images/users/$it"
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_profile_placeholder)
                .error(R.drawable.ic_profile_placeholder)
                .circleCrop()
                .into(binding.imageViewProfileLarge)
        } ?: run {
            binding.imageViewProfileLarge.setImageResource(R.drawable.ic_profile_placeholder)
        }
    }

    private fun setupButtons() {
        binding.buttonEditProfile.setOnClickListener {
            // Ouvrir la version web pour modifier le profil
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://localhost:4200/profile"))
            startActivity(intent)
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH)
        return format.format(date)
    }
    
    private fun formatDateOfBirth(dateString: String?): String {
        if (dateString == null) return "Non renseigné"
        
        return try {
            // Parse la date du backend (format: yyyy-MM-dd)
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            
            // Format en français (ex: 15 janvier 2000)
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH)
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}