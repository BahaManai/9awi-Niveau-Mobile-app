package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.local.dao.UserDao
import com.example.kawi_niveau_mobile_app.data.network.QuizApiService
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.requests.QuizSubmissionRequest
import com.example.kawi_niveau_mobile_app.data.responses.QuizAttemptResponse
import com.example.kawi_niveau_mobile_app.data.responses.QuizResponse
import com.example.kawi_niveau_mobile_app.data.responses.ResultatQuizResponse
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val quizApiService: QuizApiService,
    private val userDao: UserDao
) : BaseRepository() {

    suspend fun getQuizByModuleId(moduleId: Long): Resource<QuizResponse> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            quizApiService.getQuizByModuleId("Bearer $token", moduleId)
        }
    }

    suspend fun getQuizById(quizId: Long): Resource<QuizResponse> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            quizApiService.getQuizById("Bearer $token", quizId)
        }
    }

    suspend fun submitQuiz(quizId: Long, submission: QuizSubmissionRequest): Resource<ResultatQuizResponse> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            quizApiService.submitQuiz("Bearer $token", quizId, submission)
        }
    }

    suspend fun getUserQuizAttempts(quizId: Long): Resource<List<QuizAttemptResponse>> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            quizApiService.getUserQuizAttempts("Bearer $token", quizId)
        }
    }

    suspend fun getBestScore(quizId: Long): Resource<QuizAttemptResponse> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            quizApiService.getBestScore("Bearer $token", quizId)
        }
    }

    suspend fun getResultatDetails(resultatId: Long): Resource<ResultatQuizResponse> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            quizApiService.getResultatDetails("Bearer $token", resultatId)
        }
    }
}
