package com.example.kawi_niveau_mobile_app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.repository.AuthRepository
import com.example.kawi_niveau_mobile_app.data.responses.LoginResponse
import com.example.kawi_niveau_mobile_app.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.kawi_niveau_mobile_app.data.responses.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _loginResult = MutableLiveData<Resource<LoginResponse>>()
    val loginResult: LiveData<Resource<LoginResponse>> = _loginResult

    private val _registerResult = MutableLiveData<Resource<LoginResponse>>()
    val registerResult: LiveData<Resource<LoginResponse>> = _registerResult

    private val _uploadResult = MutableLiveData<Resource<UploadResponse>>()
    val uploadResult: LiveData<Resource<UploadResponse>> = _uploadResult

    // État d'authentification pour MainActivity
    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    private val _authenticatedUser = MutableLiveData<com.example.kawi_niveau_mobile_app.data.local.entity.UserEntity?>()
    val authenticatedUser: LiveData<com.example.kawi_niveau_mobile_app.data.local.entity.UserEntity?> = _authenticatedUser

    fun login(email: String, password: String) {
        _loginResult.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _loginResult.postValue(result)
        }
    }

    fun loginWithGoogle(googleIdToken: String) {
        android.util.Log.d("AuthViewModel", "Login with Google started - Token: ${googleIdToken.take(50)}...")
        _loginResult.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = authRepository.loginWithGoogle(googleIdToken)
            android.util.Log.d("AuthViewModel", "Login result: ${result.javaClass.simpleName}")
            _loginResult.postValue(result)
        }
    }

    fun register(firstName: String, lastName: String, dateOfBirth: String, email: String, password: String, phoneNumber: String?) {
        _registerResult.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = authRepository.register(firstName, lastName, dateOfBirth, email, password, phoneNumber)
            _registerResult.postValue(result)
        }
    }

    fun uploadImageAfterRegister(file: MultipartBody.Part, email: RequestBody) {
        _uploadResult.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = authRepository.uploadImageAfterRegister(file, email)
            _uploadResult.postValue(result)
        }
    }

    // Vérifier la session au démarrage
    fun checkSession() {
        viewModelScope.launch {
            val isLoggedIn = authRepository.isUserLoggedIn()
            _isAuthenticated.postValue(isLoggedIn)
            
            if (isLoggedIn) {
                val user = authRepository.getCurrentUser()
                _authenticatedUser.postValue(user)
                android.util.Log.d("AuthViewModel", "User session found: ${user?.email}")
            } else {
                android.util.Log.d("AuthViewModel", "No user session found")
            }
        }
    }

    // Déconnexion
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _isAuthenticated.postValue(false)
            _authenticatedUser.postValue(null)
            android.util.Log.d("AuthViewModel", "User logged out")
        }
    }
}
