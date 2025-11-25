package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.requests.EnrollmentRequest
import com.example.kawi_niveau_mobile_app.data.responses.CoursResponse
import com.example.kawi_niveau_mobile_app.data.responses.EnrollmentResponse
import com.example.kawi_niveau_mobile_app.data.responses.ModuleProgressResponse
import com.example.kawi_niveau_mobile_app.data.responses.ModuleResponse
import com.example.kawi_niveau_mobile_app.data.responses.ProfileResponse
import com.example.kawi_niveau_mobile_app.data.responses.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(email: String, password: String) =
        apiService.login(LoginRequest(email, password))

    suspend fun loginWithGoogle(googleIdToken: String) =
        apiService.loginWithGoogle(OAuth2LoginRequest(googleIdToken))

    suspend fun register(
        firstName: String,
        lastName: String,
        dateOfBirth: String,
        email: String,
        password: String,
        phoneNumber: String?
    ) = apiService.register(RegisterRequest(firstName, lastName, dateOfBirth, email, password, phoneNumber))

    suspend fun getProfile(token: String): Response<ProfileResponse> =
        apiService.getProfile("Bearer $token")

    suspend fun uploadImageAfterRegister(file: MultipartBody.Part, email: RequestBody): Response<UploadResponse> =
        apiService.uploadImageAfterRegister(file, email)

    suspend fun uploadProfileImage(token: String, file: MultipartBody.Part): Response<UploadResponse> =
        apiService.uploadProfileImage("Bearer $token", file)

    // ========== COURS ==========
    suspend fun getAllCours(token: String): Response<List<CoursResponse>> =
        apiService.getAllCours("Bearer $token")

    suspend fun getCoursById(token: String, id: Long): Response<CoursResponse> =
        apiService.getCoursById("Bearer $token", id)

    // ========== ENROLLMENT ==========
    suspend fun enrollInCourse(token: String, request: EnrollmentRequest): Response<EnrollmentResponse> =
        apiService.enrollInCourse("Bearer $token", request)

    suspend fun getUserEnrollments(token: String): Response<List<EnrollmentResponse>> =
        apiService.getUserEnrollments("Bearer $token")

    suspend fun getEnrollmentDetails(token: String, coursId: Long): Response<EnrollmentResponse> =
        apiService.getEnrollmentDetails("Bearer $token", coursId)

    // ========== MODULE ==========
    suspend fun getModulesByCours(token: String, coursId: Long): Response<List<ModuleResponse>> =
        apiService.getModulesByCours("Bearer $token", coursId)

    suspend fun getModuleById(token: String, id: Long): Response<ModuleResponse> =
        apiService.getModuleById("Bearer $token", id)

    // ========== MODULE PROGRESS ==========
    suspend fun getModulesWithProgress(token: String, coursId: Long): Response<List<ModuleProgressResponse>> =
        apiService.getModulesWithProgress("Bearer $token", coursId)
}