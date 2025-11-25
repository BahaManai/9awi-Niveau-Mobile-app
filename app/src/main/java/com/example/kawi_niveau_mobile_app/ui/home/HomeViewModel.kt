package com.example.kawi_niveau_mobile_app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.repository.UserRepository
import com.example.kawi_niveau_mobile_app.data.responses.ProfileResponse
import com.example.kawi_niveau_mobile_app.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _userProfile = MutableLiveData<Resource<ProfileResponse>>()
    val userProfile: LiveData<Resource<ProfileResponse>> = _userProfile

    fun getUserProfile() {
        _userProfile.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = userRepository.getProfile()
            _userProfile.postValue(result)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
