package com.example.kawi_niveau_mobile_app.ui.home.profil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.repository.UserRepository
import com.example.kawi_niveau_mobile_app.data.responses.ProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _profile = MutableLiveData<Resource<ProfileResponse>>()
    val profile: LiveData<Resource<ProfileResponse>> = _profile

    init {
        loadProfile()
    }

    fun loadProfile() {
        _profile.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = userRepository.getProfile()
            _profile.postValue(result)
        }
    }
}
