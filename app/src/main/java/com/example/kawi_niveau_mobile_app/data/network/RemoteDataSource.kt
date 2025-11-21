package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.responses.ProfileResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(email: String, password: String) =
        apiService.login(LoginRequest(email, password))

    suspend fun register(firstName: String, lastName: String, dateOfBirth: String, email: String, password: String) =
        apiService.register(RegisterRequest(firstName, lastName, dateOfBirth, email, password))

    suspend fun getUserProfile(token: String): Response<ProfileResponse> =
        apiService.getUserProfile("Bearer $token")
}