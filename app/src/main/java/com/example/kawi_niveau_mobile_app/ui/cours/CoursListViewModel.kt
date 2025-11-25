package com.example.kawi_niveau_mobile_app.ui.cours

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.repository.CoursRepository
import com.example.kawi_niveau_mobile_app.data.repository.EnrollmentRepository
import com.example.kawi_niveau_mobile_app.data.responses.CoursResponse
import com.example.kawi_niveau_mobile_app.data.responses.EnrollmentResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CoursWithEnrollment(
    val cours: CoursResponse,
    val enrollment: EnrollmentResponse? = null,
    val isEnrolled: Boolean = false
)

data class UserStats(
    val enrolledCount: Int = 0,
    val completedCount: Int = 0,
    val totalPoints: Int = 0,
    val userLevel: Int = 1,
    val overallProgress: Int = 0
)

@HiltViewModel
class CoursListViewModel @Inject constructor(
    private val coursRepository: CoursRepository,
    private val enrollmentRepository: EnrollmentRepository
) : ViewModel() {

    private val _coursList = MutableLiveData<Resource<List<CoursWithEnrollment>>>()
    val coursList: LiveData<Resource<List<CoursWithEnrollment>>> = _coursList

    private val _userStats = MutableLiveData<UserStats>()
    val userStats: LiveData<UserStats> = _userStats

    private val _enrollmentResult = MutableLiveData<Resource<EnrollmentResponse>>()
    val enrollmentResult: LiveData<Resource<EnrollmentResponse>> = _enrollmentResult

    init {
        loadCours()
    }

    fun loadCours() {
        _coursList.postValue(Resource.Loading())
        viewModelScope.launch {
            val coursResult = coursRepository.getAllCours()
            
            when (coursResult) {
                is Resource.Success -> {
                    val enrollmentsResult = enrollmentRepository.getUserEnrollments()
                    
                    when (enrollmentsResult) {
                        is Resource.Success -> {
                            val enrollments = enrollmentsResult.data
                            
                            // Combiner cours et enrollments
                            val coursWithEnrollments = coursResult.data.map { cours ->
                                val enrollment = enrollments.find { it.coursId == cours.id }
                                CoursWithEnrollment(
                                    cours = cours,
                                    enrollment = enrollment,
                                    isEnrolled = enrollment != null
                                )
                            }
                            
                            _coursList.postValue(Resource.Success(coursWithEnrollments))
                            
                            // Calculer les statistiques
                            calculateStats(enrollments)
                        }
                        is Resource.Error -> {
                            // Même si les enrollments échouent, afficher les cours
                            val coursWithEnrollments = coursResult.data.map { cours ->
                                CoursWithEnrollment(cours = cours)
                            }
                            _coursList.postValue(Resource.Success(coursWithEnrollments))
                        }
                        else -> {}
                    }
                }
                is Resource.Error -> {
                    _coursList.postValue(Resource.Error(coursResult.message ?: "Erreur inconnue"))
                }
                else -> {}
            }
        }
    }

    private fun calculateStats(enrollments: List<EnrollmentResponse>) {
        val enrolledCount = enrollments.size
        val completedCount = enrollments.count { it.progress >= 100f }
        
        // Calculer la progression globale
        val overallProgress = if (enrollments.isNotEmpty()) {
            (enrollments.sumOf { it.progress.toDouble() } / enrollments.size).toInt()
        } else {
            0
        }
        
        // Calculer les points (10 points par % de progression)
        val totalPoints = enrollments.sumOf { (it.progress * 10).toInt() }
        
        // Calculer le niveau (1 niveau tous les 500 points)
        val userLevel = (totalPoints / 500) + 1
        
        _userStats.postValue(
            UserStats(
                enrolledCount = enrolledCount,
                completedCount = completedCount,
                totalPoints = totalPoints,
                userLevel = userLevel,
                overallProgress = overallProgress
            )
        )
    }

    fun enrollInCourse(coursId: Long) {
        _enrollmentResult.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = enrollmentRepository.enrollInCourse(coursId)
            _enrollmentResult.postValue(result)
            
            if (result is Resource.Success) {
                // Recharger la liste des cours
                loadCours()
            }
        }
    }

    fun resetEnrollmentResult() {
        _enrollmentResult.value = null
    }
}
