package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.requests.QuizSubmissionRequest
import com.example.kawi_niveau_mobile_app.data.responses.QuizAttemptResponse
import com.example.kawi_niveau_mobile_app.data.responses.QuizResponse
import com.example.kawi_niveau_mobile_app.data.responses.ResultatQuizResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface QuizApiService {
    @GET("/api/quiz/module/{moduleId}")
    suspend fun getQuizByModuleId(
        @Header("Authorization") token: String,
        @Path("moduleId") moduleId: Long
    ): Response<QuizResponse>

    @GET("/api/quiz/{quizId}")
    suspend fun getQuizById(
        @Header("Authorization") token: String,
        @Path("quizId") quizId: Long
    ): Response<QuizResponse>

    @POST("/api/quiz-resultats/quiz/{quizId}/submit")
    suspend fun submitQuiz(
        @Header("Authorization") token: String,
        @Path("quizId") quizId: Long,
        @Body submission: QuizSubmissionRequest
    ): Response<ResultatQuizResponse>

    @GET("/api/quiz-resultats/quiz/{quizId}/attempts")
    suspend fun getUserQuizAttempts(
        @Header("Authorization") token: String,
        @Path("quizId") quizId: Long
    ): Response<List<QuizAttemptResponse>>

    @GET("/api/quiz-resultats/quiz/{quizId}/best-score")
    suspend fun getBestScore(
        @Header("Authorization") token: String,
        @Path("quizId") quizId: Long
    ): Response<QuizAttemptResponse>

    @GET("/api/quiz-resultats/{resultatId}")
    suspend fun getResultatDetails(
        @Header("Authorization") token: String,
        @Path("resultatId") resultatId: Long
    ): Response<ResultatQuizResponse>
}
