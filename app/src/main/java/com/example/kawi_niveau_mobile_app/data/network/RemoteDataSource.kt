package com.example.kawi_niveau_mobile_app.data.network

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
}