package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.local.dao.UserDao
import com.example.kawi_niveau_mobile_app.data.local.entity.UserEntity
import com.example.kawi_niveau_mobile_app.data.local.entity.toUserEntity
import com.example.kawi_niveau_mobile_app.data.network.AuthApiService
import com.example.kawi_niveau_mobile_app.data.network.ProfileApiService
import com.example.kawi_niveau_mobile_app.data.network.LoginRequest
import com.example.kawi_niveau_mobile_app.data.network.OAuth2LoginRequest
import com.example.kawi_niveau_mobile_app.data.network.RegisterRequest
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.responses.LoginResponse
import com.example.kawi_niveau_mobile_app.data.responses.UploadResponse
import javax.inject.Inject
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val profileApiService: ProfileApiService,
    private val userDao: UserDao
) : BaseRepository() {

    suspend fun login(email: String, password: String): Resource<LoginResponse> {
        return safeApiCall {
            authApiService.login(LoginRequest(email, password))
        }.also { result ->
            if (result is Resource.Success) {
                // Vérifier le rôle
                if (result.data.role != "ETUDIANT") {
                    android.util.Log.e("AuthRepository", "Access denied - Role is ${result.data.role}, expected ETUDIANT")
                    return@also
                }
                // Sauvegarder dans Room
                result.data.token?.let { token ->
                    saveUserSession(token)
                }
            }
        }
    }

    private suspend fun saveUserSession(token: String) {
        try {
            // Récupérer le profil complet de l'utilisateur
            val profileResponse = profileApiService.getProfile("Bearer $token")
            if (profileResponse.isSuccessful && profileResponse.body() != null) {
                val profile = profileResponse.body()!!
                // Convertir et sauvegarder dans Room
                val userEntity = profile.toUserEntity(token)
                userDao.insertUser(userEntity)
                android.util.Log.d("AuthRepository", "User saved to Room: ${userEntity.email}")
            }
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Error saving user session: ${e.message}")
        }
    }

    suspend fun loginWithGoogle(googleIdToken: String): Resource<LoginResponse> {
        android.util.Log.d("AuthRepository", "Sending Google token to backend")
        return safeApiCall {
            authApiService.loginWithGoogle(OAuth2LoginRequest(googleIdToken))
        }.also { result ->
            when (result) {
                is Resource.Success -> {
                    android.util.Log.d("AuthRepository", "Backend success - Token: ${if (result.data.token != null) "present" else "null"}")
                    android.util.Log.d("AuthRepository", "User role: ${result.data.role}")
                    
                    // Vérifier le rôle
                    if (result.data.role != "ETUDIANT") {
                        android.util.Log.e("AuthRepository", "Access denied - Role is ${result.data.role}, expected ETUDIANT")
                        return@also
                    }
                    
                    // Sauvegarder dans Room
                    result.data.token?.let { token ->
                        saveUserSession(token)
                        android.util.Log.d("AuthRepository", "JWT saved to Room")
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
            authApiService.register(RegisterRequest(firstName, lastName, dateOfBirth, email, password, phoneNumber))
        }
    }

    suspend fun uploadImageAfterRegister(file: MultipartBody.Part, email: RequestBody): Resource<UploadResponse> {
        return safeApiCall {
            profileApiService.uploadImageAfterRegister(file, email)
        }
    }

    // Gestion de session
    suspend fun getCurrentUser(): UserEntity? {
        return userDao.getUser()
    }

    fun getCurrentUserLiveData(): androidx.lifecycle.LiveData<UserEntity?> {
        return userDao.getUserLiveData()
    }

    suspend fun isUserLoggedIn(): Boolean {
        return userDao.isUserLoggedIn()
    }

    fun isUserLoggedInLiveData(): androidx.lifecycle.LiveData<Boolean> {
        return userDao.isUserLoggedInLiveData()
    }

    suspend fun logout() {
        userDao.clearUser()
    }
}
