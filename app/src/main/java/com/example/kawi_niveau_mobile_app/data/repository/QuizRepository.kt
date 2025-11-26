package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.UserPreferences
import com.example.kawi_niveau_mobile_app.data.network.RemoteDataSource
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.requests.QuizSubmissionRequest
import com.example.kawi_niveau_mobile_app.data.responses.QuizAttemptResponse
import com.example.kawi_niveau_mobile_app.data.responses.QuizResponse
import com.example.kawi_niveau_mobile_app.data.responses.ResultatQuizResponse
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getQuizByModuleId(moduleId: Long): Resource<QuizResponse> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            remoteDataSource.getQuizByModuleId(token, moduleId)
        }
    }

    suspend fun getQuizById(quizId: Long): Resource<QuizResponse> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            remoteDataSource.getQuizById(token, quizId)
        }
    }

    suspend fun submitQuiz(quizId: Long, submission: QuizSubmissionRequest): Resource<ResultatQuizResponse> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            remoteDataSource.submitQuiz(token, quizId, submission)
        }
    }

    suspend fun getUserQuizAttempts(quizId: Long): Resource<List<QuizAttemptResponse>> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            remoteDataSource.getUserQuizAttempts(token, quizId)
        }
    }

    suspend fun getBestScore(quizId: Long): Resource<QuizAttemptResponse> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            remoteDataSource.getBestScore(token, quizId)
        }
    }

    suspend fun getResultatDetails(resultatId: Long): Resource<ResultatQuizResponse> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            remoteDataSource.getResultatDetails(token, resultatId)
        }
    }
}
