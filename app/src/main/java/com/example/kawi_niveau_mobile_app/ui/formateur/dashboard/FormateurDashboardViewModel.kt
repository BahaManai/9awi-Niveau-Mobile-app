package com.example.kawi_niveau_mobile_app.ui.formateur.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kawi_niveau_mobile_app.data.dto.CoursParNiveauDto
import com.example.kawi_niveau_mobile_app.data.dto.FormateurStatsResponse
import com.example.kawi_niveau_mobile_app.data.dto.ParcoursResponse
import com.example.kawi_niveau_mobile_app.data.dto.ParcoursProgressionStatsResponse
import com.example.kawi_niveau_mobile_app.data.repository.FormateurRepository
import com.example.kawi_niveau_mobile_app.data.repository.ParcoursRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormateurDashboardViewModel @Inject constructor(
    private val formateurRepository: FormateurRepository,
    private val parcoursRepository: ParcoursRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormateurDashboardUiState())
    val uiState: StateFlow<FormateurDashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                // Charger les statistiques du formateur
                val statsResult = formateurRepository.getFormateurStats()
                if (statsResult.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        formateurStats = statsResult.getOrNull()
                    )
                }
                
                // Charger les parcours du formateur
                val parcoursResult = parcoursRepository.getParcoursFormateur()
                if (parcoursResult.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        parcoursList = parcoursResult.getOrNull() ?: emptyList()
                    )
                }
                
                _uiState.value = _uiState.value.copy(isLoading = false)
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erreur lors du chargement des données"
                )
            }
        }
    }

    fun loadParcoursProgressionStats(parcoursId: Long) {
        viewModelScope.launch {
            try {
                val result = parcoursRepository.getParcoursProgressionStats(parcoursId)
                if (result.isSuccess) {
                    val stats = result.getOrNull()
                    if (stats != null) {
                        _uiState.value = _uiState.value.copy(
                            selectedParcoursStats = stats
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Erreur lors du chargement des statistiques"
                )
            }
        }
    }

    fun clearSelectedParcoursStats() {
        _uiState.value = _uiState.value.copy(selectedParcoursStats = null)
    }
}

data class FormateurDashboardUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val formateurStats: FormateurStatsResponse? = null,
    val parcoursList: List<ParcoursResponse> = emptyList(),
    val selectedParcoursStats: ParcoursProgressionStatsResponse? = null
)