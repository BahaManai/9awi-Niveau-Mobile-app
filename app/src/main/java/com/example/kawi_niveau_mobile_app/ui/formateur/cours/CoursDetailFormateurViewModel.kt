package com.example.kawi_niveau_mobile_app.ui.formateur.cours

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.responses.CoursResponse
import com.example.kawi_niveau_mobile_app.data.responses.ModuleResponse
import com.example.kawi_niveau_mobile_app.data.dto.CoursStatsResponse
import com.example.kawi_niveau_mobile_app.data.repository.CoursRepository
import com.example.kawi_niveau_mobile_app.data.repository.ModuleRepository
import com.example.kawi_niveau_mobile_app.data.repository.FormateurRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoursDetailFormateurViewModel @Inject constructor(
    private val coursRepository: CoursRepository,
    private val moduleRepository: ModuleRepository,
    private val formateurRepository: FormateurRepository
) : ViewModel() {

    private val _cours = MutableLiveData<Resource<CoursResponse>>()
    val cours: LiveData<Resource<CoursResponse>> = _cours

    private val _coursStats = MutableLiveData<Resource<CoursStatsResponse>>()
    val coursStats: LiveData<Resource<CoursStatsResponse>> = _coursStats

    private val _modules = MutableLiveData<Resource<List<ModuleResponse>>>()
    val modules: LiveData<Resource<List<ModuleResponse>>> = _modules

    fun loadCoursDetail(coursId: Long) {
        viewModelScope.launch {
            _cours.value = Resource.Loading()
            try {
                val response = coursRepository.getCoursById(coursId)
                _cours.value = response
                
                // Charger les modules en même temps
                loadModules(coursId)
            } catch (e: Exception) {
                _cours.value = Resource.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    fun loadCoursStats(coursId: Long) {
        viewModelScope.launch {
            _coursStats.value = Resource.Loading()
            try {
                val stats = formateurRepository.getCoursStats(coursId)
                _coursStats.value = Resource.Success(stats)
            } catch (e: Exception) {
                _coursStats.value = Resource.Error(e.message ?: "Erreur lors du chargement des statistiques")
            }
        }
    }

    private fun loadModules(coursId: Long) {
        viewModelScope.launch {
            _modules.value = Resource.Loading()
            try {
                val modules = moduleRepository.getModulesByCours(coursId)
                _modules.value = modules
            } catch (e: Exception) {
                _modules.value = Resource.Error(e.message ?: "Erreur lors du chargement des modules")
            }
        }
    }
}