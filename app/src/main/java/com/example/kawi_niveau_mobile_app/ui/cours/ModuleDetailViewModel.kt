package com.example.kawi_niveau_mobile_app.ui.cours

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.repository.EnrollmentRepository
import com.example.kawi_niveau_mobile_app.data.repository.LeconRepository
import com.example.kawi_niveau_mobile_app.data.repository.ModuleRepository
import com.example.kawi_niveau_mobile_app.data.responses.EnrollmentResponse
import com.example.kawi_niveau_mobile_app.data.responses.LeconResponse
import com.example.kawi_niveau_mobile_app.data.responses.ModuleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LeconWithCompletion(
    val lecon: LeconResponse,
    var completed: Boolean = false
)

@HiltViewModel
class ModuleDetailViewModel @Inject constructor(
    private val moduleRepository: ModuleRepository,
    private val leconRepository: LeconRepository,
    private val enrollmentRepository: EnrollmentRepository
) : ViewModel() {

    private val _module = MutableLiveData<Resource<ModuleResponse>>()
    val module: LiveData<Resource<ModuleResponse>> = _module

    private val _lecons = MutableLiveData<Resource<List<LeconWithCompletion>>>()
    val lecons: LiveData<Resource<List<LeconWithCompletion>>> = _lecons

    private val _completionResult = MutableLiveData<Resource<EnrollmentResponse>>()
    val completionResult: LiveData<Resource<EnrollmentResponse>> = _completionResult

    private var coursId: Long = 0L
    private val completedLeconIds = mutableSetOf<Long>()

    fun loadModuleDetail(moduleId: Long) {
        loadModule(moduleId)
        loadLecons(moduleId)
    }

    private fun loadModule(moduleId: Long) {
        _module.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = moduleRepository.getModuleById(moduleId)
            _module.postValue(result)
            
            if (result is Resource.Success) {
                coursId = result.data.coursId
                loadCompletedLecons()
            }
        }
    }

    private fun loadCompletedLecons() {
        if (coursId == 0L) return
        
        viewModelScope.launch {
            val result = enrollmentRepository.getCompletedLeconIds(coursId)
            if (result is Resource.Success) {
                completedLeconIds.clear()
                completedLeconIds.addAll(result.data)
                
                // Mettre à jour la liste des leçons si déjà chargée
                val currentLecons = _lecons.value
                if (currentLecons is Resource.Success) {
                    val updatedLecons = currentLecons.data.map { leconWithCompletion ->
                        leconWithCompletion.copy(
                            completed = completedLeconIds.contains(leconWithCompletion.lecon.id)
                        )
                    }
                    _lecons.postValue(Resource.Success(updatedLecons))
                }
            }
        }
    }

    private fun loadLecons(moduleId: Long) {
        _lecons.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = leconRepository.getLeconsByModule(moduleId)
            
            when (result) {
                is Resource.Success -> {
                    val leconsWithCompletion = result.data.map { lecon ->
                        LeconWithCompletion(
                            lecon = lecon,
                            completed = completedLeconIds.contains(lecon.id)
                        )
                    }
                    _lecons.postValue(Resource.Success(leconsWithCompletion))
                }
                is Resource.Error -> {
                    _lecons.postValue(Resource.Error(result.message ?: "Erreur inconnue"))
                }
                else -> {}
            }
        }
    }

    fun toggleLeconCompletion(leconId: Long, currentlyCompleted: Boolean) {
        _completionResult.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = if (currentlyCompleted) {
                // Démarquer
                enrollmentRepository.unmarkLeconAsCompleted(coursId, leconId)
            } else {
                // Marquer comme complétée
                enrollmentRepository.markLeconAsCompleted(coursId, leconId)
            }
            
            _completionResult.postValue(result)
            
            if (result is Resource.Success) {
                // Mettre à jour localement
                if (currentlyCompleted) {
                    completedLeconIds.remove(leconId)
                } else {
                    completedLeconIds.add(leconId)
                }
                
                // Mettre à jour la liste des leçons
                val currentLecons = _lecons.value
                if (currentLecons is Resource.Success) {
                    val updatedLecons = currentLecons.data.map { leconWithCompletion ->
                        if (leconWithCompletion.lecon.id == leconId) {
                            leconWithCompletion.copy(completed = !currentlyCompleted)
                        } else {
                            leconWithCompletion
                        }
                    }
                    _lecons.postValue(Resource.Success(updatedLecons))
                }
            }
        }
    }

    fun resetCompletionResult() {
        _completionResult.value = null
    }

    fun allLeconsCompleted(): Boolean {
        val currentLecons = _lecons.value
        return if (currentLecons is Resource.Success) {
            currentLecons.data.isNotEmpty() && currentLecons.data.all { it.completed }
        } else {
            false
        }
    }
}
