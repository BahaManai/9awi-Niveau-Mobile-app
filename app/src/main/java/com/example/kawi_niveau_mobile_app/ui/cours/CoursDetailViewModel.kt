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
            android.util.Log.d("CoursDetailViewModel", "Loading modules for coursId: $coursId")
            
            // D'abord essayer avec progression
            val resultWithProgress = moduleRepository.getModulesWithProgress(coursId)
            android.util.Log.d("CoursDetailViewModel", "Modules with progress result: $resultWithProgress")
            
            when (resultWithProgress) {
                is Resource.Success -> {
                    android.util.Log.d("CoursDetailViewModel", "Modules loaded: ${resultWithProgress.data.size} modules")
                    _modules.postValue(resultWithProgress)
                }
                is Resource.Error -> {
                    android.util.Log.e("CoursDetailViewModel", "Error loading modules with progress: ${resultWithProgress.message}")
                    
                    // Si ça échoue, essayer sans progression
                    android.util.Log.d("CoursDetailViewModel", "Trying to load modules without progress...")
                    val resultBasic = moduleRepository.getModulesByCours(coursId)
                    android.util.Log.d("CoursDetailViewModel", "Basic modules result: $resultBasic")
                    
                    when (resultBasic) {
                        is Resource.Success -> {
                            // Convertir ModuleResponse en ModuleProgressResponse
                            val modulesWithProgress = resultBasic.data.map { module ->
                                ModuleProgressResponse(
                                    id = module.id,
                                    titre = module.titre,
                                    contenu = module.contenu,
                                    ordre = module.ordre,
                                    coursId = module.coursId,
                                    createdAt = module.createdAt,
                                    updatedAt = module.updatedAt,
                                    totalLecons = 0,
                                    leconsCompletees = 0,
                                    progressionLecons = 0.0f,
                                    hasQuiz = false,
                                    quizId = null,
                                    quizTitre = null,
                                    quizPassed = false,
                                    bestScore = null,
                                    totalAttempts = 0
                                )
                            }
                            _modules.postValue(Resource.Success(modulesWithProgress))
                        }
                        is Resource.Error -> {
                            android.util.Log.e("CoursDetailViewModel", "Error loading basic modules: ${resultBasic.message}")
                            _modules.postValue(Resource.Error(resultBasic.message))
                        }
                        else -> {}
                    }
                }
                else -> {}
            }
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
