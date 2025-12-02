package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.UserPreferences
import com.example.kawi_niveau_mobile_app.data.network.ProfileApiService
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.responses.ProfileResponse
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val profileApiService: ProfileApiService,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getProfile(): Resource<ProfileResponse> {
        return safeApiCall {
            val token = userPreferences.getToken().first()
            profileApiService.getProfile("Bearer $token")
        }
    }

    suspend fun logout() {
        userPreferences.clearToken()
    }
}