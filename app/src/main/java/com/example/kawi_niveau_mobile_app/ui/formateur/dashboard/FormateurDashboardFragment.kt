package com.example.kawi_niveau_mobile_app.ui.formateur.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kawi_niveau_mobile_app.databinding.FragmentFormateurDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FormateurDashboardFragment : Fragment() {

    private var _binding: FragmentFormateurDashboardBinding? = null
    private val binding get() = _binding!!

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
        
        binding.textDashboard.text = "Bienvenue dans l'espace Formateur !\n\nVous pourrez bientôt gérer vos cours, modules, leçons et quiz."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
