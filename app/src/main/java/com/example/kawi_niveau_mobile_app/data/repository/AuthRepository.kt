package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.UserPreferences
import com.example.kawi_niveau_mobile_app.data.network.RemoteDataSource
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.responses.LoginResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun login(email: String, password: String): Resource<LoginResponse> {
        return safeApiCall {
            remoteDataSource.login(email, password)
        }.also { result ->
            if (result is Resource.Success) {
                result.data.token?.let { userPreferences.saveToken(it) }
            }
        }
    }

    suspend fun register(firstName: String, lastName: String, dateOfBirth: String, email: String, password: String): Resource<LoginResponse> {
        return safeApiCall {
            remoteDataSource.register(firstName, lastName, dateOfBirth, email, password)
        }
    }
}