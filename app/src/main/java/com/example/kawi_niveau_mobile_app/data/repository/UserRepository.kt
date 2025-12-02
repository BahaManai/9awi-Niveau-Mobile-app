package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.local.dao.UserDao
import com.example.kawi_niveau_mobile_app.data.network.ProfileApiService
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.responses.ProfileResponse
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val profileApiService: ProfileApiService,
    private val userDao: UserDao
) : BaseRepository() {

    suspend fun getProfile(): Resource<ProfileResponse> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        
        return safeApiCall {
            profileApiService.getProfile("Bearer $token")
        }
    }

    suspend fun logout() {
        userDao.clearUser()
    }
}