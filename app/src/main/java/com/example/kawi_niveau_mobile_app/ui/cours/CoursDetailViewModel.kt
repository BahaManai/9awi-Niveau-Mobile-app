package com.example.kawi_niveau_mobile_app.ui.cours

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.repository.CoursRepository
import com.example.kawi_niveau_mobile_app.data.repository.EnrollmentRepository
import com.example.kawi_niveau_mobile_app.data.repository.ModuleRepository
import com.example.kawi_niveau_mobile_app.data.responses.CoursResponse
import com.example.kawi_niveau_mobile_app.data.responses.EnrollmentResponse
import com.example.kawi_niveau_mobile_app.data.responses.ModuleProgressResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoursDetailViewModel @Inject constructor(
    private val coursRepository: CoursRepository,
    private val enrollmentRepository: EnrollmentRepository,
    private val moduleRepository: ModuleRepository
) : ViewModel() {

    private val _cours = MutableLiveData<Resource<CoursResponse>>()
    val cours: LiveData<Resource<CoursResponse>> = _cours

    private val _enrollment = MutableLiveData<Resource<EnrollmentResponse?>>()
    val enrollment: LiveData<Resource<EnrollmentResponse?>> = _enrollment

    private val _modules = MutableLiveData<Resource<List<ModuleProgressResponse>>>()
    val modules: LiveData<Resource<List<ModuleProgressResponse>>> = _modules

    private val _enrollmentResult = MutableLiveData<Resource<EnrollmentResponse>>()
    val enrollmentResult: LiveData<Resource<EnrollmentResponse>> = _enrollmentResult

    fun loadCoursDetail(coursId: Long) {
        loadCours(coursId)
        loadEnrollment(coursId)
        loadModules(coursId)
    }

    private fun loadCours(coursId: Long) {
        _cours.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = coursRepository.getCoursById(coursId)
            _cours.postValue(result)
        }
    }

    private fun loadEnrollment(coursId: Long) {
        _enrollment.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = enrollmentRepository.getEnrollmentDetails(coursId)
            when (result) {
                is Resource.Success -> {
                    _enrollment.postValue(Resource.Success(result.data))
                }
                is Resource.Error -> {
                    // Pas inscrit, ce n'est pas une erreur
                    _enrollment.postValue(Resource.Success(null))
                }
                else -> {}
            }
        }
    }

    private fun loadModules(coursId: Long) {
        _modules.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = moduleRepository.getModulesWithProgress(coursId)
            _modules.postValue(result)
        }
    }

    fun enrollInCourse(coursId: Long) {
        _enrollmentResult.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = enrollmentRepository.enrollInCourse(coursId)
            _enrollmentResult.postValue(result)

            if (result is Resource.Success) {
                // Recharger les données
                loadEnrollment(coursId)
                loadModules(coursId)
            }
        }
    }

    fun resetEnrollmentResult() {
        _enrollmentResult.value = null
    }
}
