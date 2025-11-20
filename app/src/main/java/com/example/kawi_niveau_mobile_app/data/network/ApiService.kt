package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.responses.LoginResponse
import com.example.kawi_niveau_mobile_app.data.responses.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<LoginResponse>

    @GET("api/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<User>
}

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)