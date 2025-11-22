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

    fun login(email: String, password: String) {
        _loginResult.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = authRepository.login(email, password)
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
}