package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.responses.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/api/auth/google")
    suspend fun loginWithGoogle(@Body request: OAuth2LoginRequest): Response<LoginResponse>

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>
}
