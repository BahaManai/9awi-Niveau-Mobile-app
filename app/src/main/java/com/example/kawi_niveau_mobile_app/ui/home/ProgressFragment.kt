package com.example.kawi_niveau_mobile_app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kawi_niveau_mobile_app.databinding.FragmentProgressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: Implémenter les statistiques de progrès
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
