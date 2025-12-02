package com.example.kawi_niveau_mobile_app.ui.formateur.cours

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kawi_niveau_mobile_app.databinding.FragmentFormateurCoursListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FormateurCoursListFragment : Fragment() {

    private var _binding: FragmentFormateurCoursListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormateurCoursListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.textCours.text = "Liste des cours (à venir dans l'étape 2)"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
