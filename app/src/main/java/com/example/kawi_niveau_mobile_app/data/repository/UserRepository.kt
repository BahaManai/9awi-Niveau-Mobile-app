package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.UserPreferences
import com.example.kawi_niveau_mobile_app.data.network.RemoteDataSource
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.responses.User
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getUserProfile(): Resource<User> {
        return safeApiCall {
            val token = userPreferences.getToken().first()
            remoteDataSource.getUserProfile(token)
        }
    }

    suspend fun logout() {
        userPreferences.clearToken()
    }
}