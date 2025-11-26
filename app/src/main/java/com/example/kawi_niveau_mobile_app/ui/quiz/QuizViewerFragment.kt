package com.example.kawi_niveau_mobile_app.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.responses.QuestionResponse
import com.example.kawi_niveau_mobile_app.data.responses.QuizResponse
import com.example.kawi_niveau_mobile_app.data.responses.ResultatQuizResponse
import com.example.kawi_niveau_mobile_app.databinding.FragmentQuizViewerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizViewerFragment : Fragment() {

    private var _binding: FragmentQuizViewerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuizViewerViewModel by viewModels()

    private var quizId: Long = 0L
    private var moduleId: Long = 0L
    private var quiz: QuizResponse? = null
    private var questions: List<QuestionResponse> = emptyList()

    // État du quiz
    private var quizStarted = false
    private var quizFinished = false
    private var currentQuestionIndex = 0
    private val reponses = mutableMapOf<Long, String>()
    private var startTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quizId = arguments?.getLong("quizId") ?: 0L
        moduleId = arguments?.getLong("moduleId") ?: 0L

        setupToolbar()
        setupObservers()
        setupClickListeners()

        viewModel.loadQuiz(quizId)
        viewModel.loadAttempts(quizId)
        viewModel.loadBestScore(quizId)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupObservers() {
        viewModel.quiz.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.layoutWelcome.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    quiz = result.data
                    questions = result.data.questions ?: emptyList()
                    showWelcomeScreen()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.submitResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    showResultScreen(result.data)
                    viewModel.resetSubmitResult()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetSubmitResult()
                }
                null -> {}
            }
        }

        viewModel.attempts.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success) {
                val attempts = result.data
                if (attempts.isNotEmpty()) {
                    binding.textViewPreviousAttempts.visibility = View.VISIBLE
                    binding.textViewAttemptsCount.visibility = View.VISIBLE
                    binding.textViewAttemptsCount.text = "${attempts.size} tentative(s)"
                }
            }
        }

        viewModel.bestScore.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success) {
                binding.textViewBestScore.visibility = View.VISIBLE
                binding.textViewBestScore.text = "Meilleur score: ${result.data.score}%"
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonStartQuiz.setOnClickListener {
            startQuiz()
        }

        binding.buttonPrevious.setOnClickListener {
            previousQuestion()
        }

        binding.buttonNext.setOnClickListener {
            nextQuestion()
        }

        binding.buttonSubmit.setOnClickListener {
            submitQuiz()
        }

        binding.buttonRetry.setOnClickListener {
            startQuiz()
        }

        binding.buttonBackToModule.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showWelcomeScreen() {
        quiz?.let {
            binding.layoutWelcome.visibility = View.VISIBLE
            binding.layoutQuiz.visibility = View.GONE
            binding.layoutResult.visibility = View.GONE

            binding.textViewQuizTitle.text = it.titre
            binding.textViewQuizDescription.text = it.description ?: "Aucune description"
            binding.textViewQuestionsCount.text = "${questions.size} question(s)"
        }
    }

    private fun startQuiz() {
        quizStarted = true
        quizFinished = false
        currentQuestionIndex = 0
        reponses.clear()
        startTime = System.currentTimeMillis()

        binding.layoutWelcome.visibility = View.GONE
        binding.layoutQuiz.visibility = View.VISIBLE
        binding.layoutResult.visibility = View.GONE

        showQuestion()
    }

    private fun showQuestion() {
        if (questions.isEmpty()) return

        val question = questions[currentQuestionIndex]
        binding.textViewQuestionNumber.text = "Question ${currentQuestionIndex + 1}/${questions.size}"
        binding.textViewQuestion.text = question.question
        binding.progressBarQuiz.progress = ((currentQuestionIndex + 1) * 100) / questions.size

        // Afficher les options
        binding.radioGroupOptions.removeAllViews()
        question.options.forEach { option ->
            val radioButton = android.widget.RadioButton(requireContext())
            radioButton.text = option
            radioButton.textSize = 16f
            radioButton.setPadding(16, 16, 16, 16)
            
            // Sélectionner si déjà répondu
            if (reponses[question.id] == option) {
                radioButton.isChecked = true
            }

            radioButton.setOnClickListener {
                reponses[question.id] = option
                updateNavigationButtons()
            }

            binding.radioGroupOptions.addView(radioButton)
        }

        updateNavigationButtons()
    }

    private fun updateNavigationButtons() {
        binding.buttonPrevious.isEnabled = currentQuestionIndex > 0

        if (currentQuestionIndex < questions.size - 1) {
            binding.buttonNext.visibility = View.VISIBLE
            binding.buttonSubmit.visibility = View.GONE
        } else {
            binding.buttonNext.visibility = View.GONE
            binding.buttonSubmit.visibility = View.VISIBLE
            binding.buttonSubmit.isEnabled = canSubmit()
        }
    }

    private fun previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--
            showQuestion()
        }
    }

    private fun nextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            showQuestion()
        }
    }

    private fun canSubmit(): Boolean {
        return questions.all { reponses.containsKey(it.id) }
    }

    private fun submitQuiz() {
        if (!canSubmit()) {
            Toast.makeText(requireContext(), "Veuillez répondre à toutes les questions", Toast.LENGTH_SHORT).show()
            return
        }

        val tempsPasse = ((System.currentTimeMillis() - startTime) / 1000).toInt()
        viewModel.submitQuiz(quizId, reponses, tempsPasse)
    }

    private fun showResultScreen(resultat: ResultatQuizResponse) {
        quizFinished = true

        binding.layoutWelcome.visibility = View.GONE
        binding.layoutQuiz.visibility = View.GONE
        binding.layoutResult.visibility = View.VISIBLE

        binding.textViewScore.text = "${resultat.score.toInt()}%"
        binding.textViewCorrectAnswers.text = "${resultat.reponsesCorrectes}/${resultat.nombreQuestions}"
        binding.textViewTimeSpent.text = formatTime(resultat.tempsPasse)

        // Couleur du score
        val scoreColor = when {
            resultat.score >= 80 -> R.color.success_green
            resultat.score >= 60 -> R.color.warning_orange
            else -> R.color.error_red
        }
        binding.textViewScore.setTextColor(resources.getColor(scoreColor, null))

        // Message
        binding.textViewResultMessage.text = when {
            resultat.score >= 80 -> "🎉 Excellent travail !"
            resultat.score >= 60 -> "👍 Bien joué !"
            else -> "💪 Continuez vos efforts !"
        }

        // Recharger les tentatives
        viewModel.loadAttempts(quizId)
        viewModel.loadBestScore(quizId)
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%d:%02d", minutes, secs)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
