package com.example.kawi_niveau_mobile_app.ui.cours

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.databinding.FragmentModuleDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModuleDetailFragment : Fragment() {

    private var _binding: FragmentModuleDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ModuleDetailViewModel by viewModels()
    private lateinit var leconAdapter: LeconAdapter
    private var moduleId: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModuleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moduleId = arguments?.getLong("moduleId") ?: 0L

        setupToolbar()
        setupRecyclerView()
        observeViewModel()

        viewModel.loadModuleDetail(moduleId)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        leconAdapter = LeconAdapter(
            onLeconClick = { leconId -> },
            onToggleCompletion = { leconId, currentlyCompleted ->
                viewModel.toggleLeconCompletion(leconId, currentlyCompleted)
            }
        )

        binding.recyclerViewLecons.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = leconAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.module.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val module = result.data

                    binding.textViewModuleTitre.text = module.titre
                    binding.textViewModuleContenu.text = module.contenu ?: "Aucune description"
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

        viewModel.lecons.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBarLecons.visibility = View.VISIBLE
                    binding.recyclerViewLecons.visibility = View.GONE
                    binding.layoutEmptyState.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBarLecons.visibility = View.GONE

                    if (result.data.isEmpty()) {
                        binding.recyclerViewLecons.visibility = View.GONE
                        binding.layoutEmptyState.visibility = View.VISIBLE
                    } else {
                        binding.recyclerViewLecons.visibility = View.VISIBLE
                        binding.layoutEmptyState.visibility = View.GONE
                        leconAdapter.submitList(result.data)
                    }
                    
                    updateQuizButtonState()
                }
                is Resource.Error -> {
                    binding.progressBarLecons.visibility = View.GONE
                    binding.recyclerViewLecons.visibility = View.GONE
                    binding.layoutEmptyState.visibility = View.VISIBLE
                    Toast.makeText(
                        requireContext(),
                        "Erreur: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.completionResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "✓ Mis à jour !",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.resetCompletionResult()
                }
                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Erreur: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.resetCompletionResult()
                }
                null -> {}
            }
        }

        viewModel.quiz.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBarQuiz.visibility = View.VISIBLE
                    binding.layoutQuiz.visibility = View.GONE
                    binding.layoutNoQuiz.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBarQuiz.visibility = View.GONE
                    
                    if (result.data != null) {
                        binding.layoutQuiz.visibility = View.VISIBLE
                        binding.layoutNoQuiz.visibility = View.GONE
                        
                        val quiz = result.data
                        binding.textViewQuizTitre.text = quiz.titre
                        binding.textViewQuizDescription.text = quiz.description ?: "Aucune description"
                        binding.textViewQuizQuestions.text = "${quiz.questions?.size ?: 0} question(s)"
                        
                        updateQuizButtonState()
                        
                        binding.buttonPasserQuiz.setOnClickListener {
                            try {
                                val action = ModuleDetailFragmentDirections.actionModuleDetailToQuizViewer(
                                    quizId = quiz.id,
                                    moduleId = moduleId
                                )
                                findNavController().navigate(action)
                            } catch (e: Exception) {
                                Toast.makeText(
                                    requireContext(),
                                    "Erreur de navigation: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        binding.layoutQuiz.visibility = View.GONE
                        binding.layoutNoQuiz.visibility = View.VISIBLE
                    }
                }
                is Resource.Error -> {
                    binding.progressBarQuiz.visibility = View.GONE
                    binding.layoutQuiz.visibility = View.GONE
                    binding.layoutNoQuiz.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateQuizButtonState() {
        if (binding.layoutQuiz.visibility != View.VISIBLE) return
        
        val allCompleted = viewModel.allLeconsCompleted()
        
        if (allCompleted) {
            binding.buttonPasserQuiz.isEnabled = true
            binding.buttonPasserQuiz.text = "Passer le quiz"
            binding.textViewQuizLocked.visibility = View.GONE
        } else {
            binding.buttonPasserQuiz.isEnabled = false
            binding.buttonPasserQuiz.text = "Quiz verrouillé"
            binding.textViewQuizLocked.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
