package com.example.kawi_niveau_mobile_app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kawi_niveau_mobile_app.databinding.FragmentCoursesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoursesFragment : Fragment() {

    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: Implémenter la liste des cours
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
