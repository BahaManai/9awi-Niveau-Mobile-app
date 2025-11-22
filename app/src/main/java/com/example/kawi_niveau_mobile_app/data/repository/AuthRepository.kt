package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.UserPreferences
import com.example.kawi_niveau_mobile_app.data.network.RemoteDataSource
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.responses.LoginResponse
import com.example.kawi_niveau_mobile_app.data.responses.UploadResponse
import javax.inject.Inject
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    suspend fun loginWithGoogle(googleIdToken: String): Resource<LoginResponse> {
        android.util.Log.d("AuthRepository", "Sending Google token to backend")
        return safeApiCall {
            remoteDataSource.loginWithGoogle(googleIdToken)
        }.also { result ->
            when (result) {
                is Resource.Success -> {
                    android.util.Log.d("AuthRepository", "Backend success - Token: ${if (result.data.token != null) "present" else "null"}")
                    result.data.token?.let { 
                        userPreferences.saveToken(it)
                        android.util.Log.d("AuthRepository", "JWT saved to DataStore")
                    }
                }
                is Resource.Error -> {
                    android.util.Log.e("AuthRepository", "Backend error: ${result.message}")
                }
                else -> {
                    android.util.Log.d("AuthRepository", "Loading state")
                }
            }
        }
    }

    suspend fun register(
        firstName: String,
        lastName: String,
        dateOfBirth: String,
        email: String,
        password: String,
        phoneNumber: String?
    ): Resource<LoginResponse> {
        return safeApiCall {
            remoteDataSource.register(firstName, lastName, dateOfBirth, email, password, phoneNumber)
        }
    }

    suspend fun uploadImageAfterRegister(file: MultipartBody.Part, email: RequestBody): Resource<UploadResponse> {
        return safeApiCall {
            remoteDataSource.uploadImageAfterRegister(file, email)
        }
    }
}