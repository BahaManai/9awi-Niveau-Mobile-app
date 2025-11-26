package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.requests.EnrollmentRequest
import com.example.kawi_niveau_mobile_app.data.requests.LeconCompletionRequest
import com.example.kawi_niveau_mobile_app.data.responses.CoursResponse
import com.example.kawi_niveau_mobile_app.data.responses.EnrollmentResponse
import com.example.kawi_niveau_mobile_app.data.responses.LeconResponse
import com.example.kawi_niveau_mobile_app.data.responses.LoginResponse
import com.example.kawi_niveau_mobile_app.data.responses.ModuleProgressResponse
import com.example.kawi_niveau_mobile_app.data.responses.ModuleResponse
import com.example.kawi_niveau_mobile_app.data.responses.ProfileResponse
import com.example.kawi_niveau_mobile_app.data.responses.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/api/auth/google")
    suspend fun loginWithGoogle(@Body request: OAuth2LoginRequest): Response<LoginResponse>

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("/api/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<ProfileResponse>

    @Multipart
    @POST("/api/profile/upload-image-after-register")
    suspend fun uploadImageAfterRegister(
        @Part file: MultipartBody.Part,
        @Part("email") email: RequestBody
    ): Response<UploadResponse>

    @Multipart
    @POST("/api/profile/upload-image")
    suspend fun uploadProfileImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Response<UploadResponse>

    // ========== COURS API ==========
    @GET("/api/cours")
    suspend fun getAllCours(@Header("Authorization") token: String): Response<List<CoursResponse>>

    @GET("/api/cours/{id}")
    suspend fun getCoursById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<CoursResponse>

    // ========== ENROLLMENT API ==========
    @POST("/api/enrollments")
    suspend fun enrollInCourse(
        @Header("Authorization") token: String,
        @Body request: EnrollmentRequest
    ): Response<EnrollmentResponse>

    @GET("/api/enrollments")
    suspend fun getUserEnrollments(@Header("Authorization") token: String): Response<List<EnrollmentResponse>>

    @GET("/api/enrollments/cours/{coursId}")
    suspend fun getEnrollmentDetails(
        @Header("Authorization") token: String,
        @Path("coursId") coursId: Long
    ): Response<EnrollmentResponse>

    // ========== MODULE API ==========
    @GET("/api/modules/cours/{coursId}")
    suspend fun getModulesByCours(
        @Header("Authorization") token: String,
        @Path("coursId") coursId: Long
    ): Response<List<ModuleResponse>>

    @GET("/api/modules/{id}")
    suspend fun getModuleById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<ModuleResponse>

    // ========== MODULE PROGRESS API ==========
    @GET("/api/module-progress/cours/{coursId}")
    suspend fun getModulesWithProgress(
        @Header("Authorization") token: String,
        @Path("coursId") coursId: Long
    ): Response<List<ModuleProgressResponse>>

    // ========== LECON API ==========
    @GET("/api/lecons/module/{moduleId}")
    suspend fun getLeconsByModule(
        @Header("Authorization") token: String,
        @Path("moduleId") moduleId: Long
    ): Response<List<LeconResponse>>

    @GET("/api/lecons/{id}")
    suspend fun getLeconById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<LeconResponse>

    // ========== LECON COMPLETION API ==========
    @POST("/api/enrollments/cours/{coursId}/complete-lecon")
    suspend fun markLeconAsCompleted(
        @Header("Authorization") token: String,
        @Path("coursId") coursId: Long,
        @Body request: LeconCompletionRequest
    ): Response<EnrollmentResponse>

    @DELETE("/api/enrollments/cours/{coursId}/lecons/{leconId}/completion")
    suspend fun unmarkLeconAsCompleted(
        @Header("Authorization") token: String,
        @Path("coursId") coursId: Long,
        @Path("leconId") leconId: Long
    ): Response<EnrollmentResponse>

    @GET("/api/enrollments/cours/{coursId}/completed-lecons")
    suspend fun getCompletedLeconIds(
        @Header("Authorization") token: String,
        @Path("coursId") coursId: Long
    ): Response<List<Long>>

    // ========== QUIZ API ==========
    @GET("/api/quiz/module/{moduleId}")
    suspend fun getQuizByModuleId(
        @Header("Authorization") token: String,
        @Path("moduleId") moduleId: Long
    ): Response<com.example.kawi_niveau_mobile_app.data.responses.QuizResponse>

    @GET("/api/quiz/{quizId}")
    suspend fun getQuizById(
        @Header("Authorization") token: String,
        @Path("quizId") quizId: Long
    ): Response<com.example.kawi_niveau_mobile_app.data.responses.QuizResponse>

    // ========== QUIZ RESULTS API ==========
    @POST("/api/quiz-resultats/quiz/{quizId}/submit")
    suspend fun submitQuiz(
        @Header("Authorization") token: String,
        @Path("quizId") quizId: Long,
        @Body submission: com.example.kawi_niveau_mobile_app.data.requests.QuizSubmissionRequest
    ): Response<com.example.kawi_niveau_mobile_app.data.responses.ResultatQuizResponse>

    @GET("/api/quiz-resultats/quiz/{quizId}/attempts")
    suspend fun getUserQuizAttempts(
        @Header("Authorization") token: String,
        @Path("quizId") quizId: Long
    ): Response<List<com.example.kawi_niveau_mobile_app.data.responses.QuizAttemptResponse>>

    @GET("/api/quiz-resultats/quiz/{quizId}/best-score")
    suspend fun getBestScore(
        @Header("Authorization") token: String,
        @Path("quizId") quizId: Long
    ): Response<com.example.kawi_niveau_mobile_app.data.responses.QuizAttemptResponse>

    @GET("/api/quiz-resultats/{resultatId}")
    suspend fun getResultatDetails(
        @Header("Authorization") token: String,
        @Path("resultatId") resultatId: Long
    ): Response<com.example.kawi_niveau_mobile_app.data.responses.ResultatQuizResponse>
}