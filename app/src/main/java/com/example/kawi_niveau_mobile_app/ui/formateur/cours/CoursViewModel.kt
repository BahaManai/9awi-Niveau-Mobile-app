package com.example.kawi_niveau_mobile_app.ui.formateur.cours

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kawi_niveau_mobile_app.data.requests.CoursRequest
import com.example.kawi_niveau_mobile_app.data.responses.CoursResponse
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.repository.CoursRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CoursViewModel @Inject constructor(
    private val coursRepository: CoursRepository
) : ViewModel() {

    private val _cours = MutableLiveData<List<CoursResponse>>()
    val cours: LiveData<List<CoursResponse>> = _cours

    private val _coursDetail = MutableLiveData<CoursResponse>()
    val coursDetail: LiveData<CoursResponse> = _coursDetail

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _success = MutableLiveData<String?>()
    val success: LiveData<String?> = _success

    private val _uploadedFilename = MutableLiveData<String?>()
    val uploadedFilename: LiveData<String?> = _uploadedFilename

    fun loadMesCours() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            when (val result = coursRepository.getMesCours()) {
                is Resource.Success -> {
                    _cours.value = result.data
                    _loading.value = false
                }
                is Resource.Error -> {
                    _error.value = result.message
                    _loading.value = false
                }
                else -> {
                    _loading.value = false
                }
            }
        }
    }

    fun loadCoursById(id: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            when (val result = coursRepository.getCoursById(id)) {
                is Resource.Success -> {
                    _coursDetail.value = result.data
                    _loading.value = false
                }
                is Resource.Error -> {
                    _error.value = result.message
                    _loading.value = false
                }
                else -> {
                    _loading.value = false
                }
            }
        }
    }

    fun createCours(request: CoursRequest) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            when (val result = coursRepository.createCours(request)) {
                is Resource.Success -> {
                    _success.value = "Cours créé avec succès"
                    _loading.value = false
                    loadMesCours()
                }
                is Resource.Error -> {
                    _error.value = result.message
                    _loading.value = false
                }
                else -> {
                    _loading.value = false
                }
            }
        }
    }

    fun updateCours(id: Long, request: CoursRequest) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            when (val result = coursRepository.updateCours(id, request)) {
                is Resource.Success -> {
                    _success.value = "Cours modifié avec succès"
                    _loading.value = false
                    loadMesCours()
                }
                is Resource.Error -> {
                    _error.value = result.message
                    _loading.value = false
                }
                else -> {
                    _loading.value = false
                }
            }
        }
    }

    fun archiveCours(id: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            when (val result = coursRepository.archiveCours(id)) {
                is Resource.Success -> {
                    _success.value = "Cours archivé avec succès"
                    _loading.value = false
                    loadMesCours()
                }
                is Resource.Error -> {
                    _error.value = result.message
                    _loading.value = false
                }
                else -> {
                    _loading.value = false
                }
            }
        }
    }

    fun unarchiveCours(id: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            when (val result = coursRepository.unarchiveCours(id)) {
                is Resource.Success -> {
                    _success.value = "Cours réactivé avec succès"
                    _loading.value = false
                    loadMesCours()
                }
                is Resource.Error -> {
                    _error.value = result.message
                    _loading.value = false
                }
                else -> {
                    _loading.value = false
                }
            }
        }
    }

    fun uploadThumbnail(file: File) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            when (val result = coursRepository.uploadThumbnail(file)) {
                is Resource.Success -> {
                    _uploadedFilename.value = result.data.filename
                    _loading.value = false
                }
                is Resource.Error -> {
                    _error.value = result.message
                    _loading.value = false
                }
                else -> {
                    _loading.value = false
                }
            }
        }
    }

    fun clearMessages() {
        _error.value = null
        _success.value = null
    }
}
