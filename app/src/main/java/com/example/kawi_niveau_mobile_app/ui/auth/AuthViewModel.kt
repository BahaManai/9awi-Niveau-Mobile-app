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

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _loginResult = MutableLiveData<Resource<LoginResponse>>()
    val loginResult: LiveData<Resource<LoginResponse>> = _loginResult

    private val _registerResult = MutableLiveData<Resource<LoginResponse>>()
    val registerResult: LiveData<Resource<LoginResponse>> = _registerResult

    fun login(username: String, password: String) {
        _loginResult.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = authRepository.login(username, password)
            _loginResult.postValue(result)
        }
    }

    fun register(username: String, email: String, password: String) {
        _registerResult.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = authRepository.register(username, email, password)
            _registerResult.postValue(result)
        }
    }
}