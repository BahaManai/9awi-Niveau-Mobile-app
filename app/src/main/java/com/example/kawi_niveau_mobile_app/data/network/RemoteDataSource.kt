package com.example.kawi_niveau_mobile_app.data.network

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(username: String, password: String) =
        apiService.login(LoginRequest(username, password))

    suspend fun register(username: String, email: String, password: String) =
        apiService.register(RegisterRequest(username, email, password))

    suspend fun getUserProfile(token: String) =
        apiService.getUserProfile("Bearer $token")
}