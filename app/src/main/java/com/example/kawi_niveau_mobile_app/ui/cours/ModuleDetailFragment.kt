package com.example.kawi_niveau_mobile_app.ui.cours

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.kawi_niveau_mobile_app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModuleDetailFragment : Fragment() {

    private var moduleId: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_module_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Récupérer l'argument moduleId
        moduleId = arguments?.getLong("moduleId") ?: 0L
        
        // Placeholder - sera implémenté avec la Fonctionnalité 3
        view.findViewById<TextView>(R.id.textViewModuleId)?.text = 
            "Module ID: $moduleId\n\nCette page sera implémentée dans la Fonctionnalité 3"
    }
}
