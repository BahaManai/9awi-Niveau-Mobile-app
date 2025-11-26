package com.example.kawi_niveau_mobile_app.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.repository.QuizRepository
import com.example.kawi_niveau_mobile_app.data.requests.QuizSubmissionRequest
import com.example.kawi_niveau_mobile_app.data.responses.QuizAttemptResponse
import com.example.kawi_niveau_mobile_app.data.responses.QuizResponse
import com.example.kawi_niveau_mobile_app.data.responses.ResultatQuizResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewerViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _quiz = MutableLiveData<Resource<QuizResponse>>()
    val quiz: LiveData<Resource<QuizResponse>> = _quiz

    private val _submitResult = MutableLiveData<Resource<ResultatQuizResponse>>()
    val submitResult: LiveData<Resource<ResultatQuizResponse>> = _submitResult

    private val _attempts = MutableLiveData<Resource<List<QuizAttemptResponse>>>()
    val attempts: LiveData<Resource<List<QuizAttemptResponse>>> = _attempts

    private val _bestScore = MutableLiveData<Resource<QuizAttemptResponse>>()
    val bestScore: LiveData<Resource<QuizAttemptResponse>> = _bestScore

    fun loadQuiz(quizId: Long) {
        _quiz.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = quizRepository.getQuizById(quizId)
            _quiz.postValue(result)
        }
    }

    fun submitQuiz(quizId: Long, reponses: Map<Long, String>, tempsPasse: Int) {
        _submitResult.postValue(Resource.Loading())
        viewModelScope.launch {
            val submission = QuizSubmissionRequest(reponses, tempsPasse)
            val result = quizRepository.submitQuiz(quizId, submission)
            _submitResult.postValue(result)
        }
    }

    fun loadAttempts(quizId: Long) {
        viewModelScope.launch {
            val result = quizRepository.getUserQuizAttempts(quizId)
            _attempts.postValue(result)
        }
    }

    fun loadBestScore(quizId: Long) {
        viewModelScope.launch {
            val result = quizRepository.getBestScore(quizId)
            _bestScore.postValue(result)
        }
    }

    fun resetSubmitResult() {
        _submitResult.value = null
    }
}
