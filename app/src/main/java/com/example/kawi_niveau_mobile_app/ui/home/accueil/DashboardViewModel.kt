package com.example.kawi_niveau_mobile_app.ui.home.accueil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.network.dto.*
import com.example.kawi_niveau_mobile_app.data.repository.ApprenantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val apprenantRepository: ApprenantRepository
) : ViewModel() {

    private val _dashboard = MutableLiveData<Resource<ApprenantDashboardResponse>>()
    val dashboard: LiveData<Resource<ApprenantDashboardResponse>> = _dashboard

    private val _stats = MutableLiveData<Resource<UserStatsDto>>()
    val stats: LiveData<Resource<UserStatsDto>> = _stats

    private val _badges = MutableLiveData<Resource<List<BadgeDto>>>()
    val badges: LiveData<Resource<List<BadgeDto>>> = _badges

    private val _challenges = MutableLiveData<Resource<List<ChallengeDto>>>()
    val challenges: LiveData<Resource<List<ChallengeDto>>> = _challenges

    private val _leaderboard = MutableLiveData<Resource<LeaderboardDto>>()
    val leaderboard: LiveData<Resource<LeaderboardDto>> = _leaderboard

    /**
     * Charger toutes les données du dashboard en une seule fois
     */
    fun loadDashboard() {
        viewModelScope.launch {
            _dashboard.value = Resource.Loading()
            val result = apprenantRepository.getDashboard()
            _dashboard.value = result
        }
    }

    /**
     * Charger uniquement les statistiques
     */
    fun loadStats() {
        viewModelScope.launch {
            _stats.value = Resource.Loading()
            val result = apprenantRepository.getStats()
            _stats.value = result
        }
    }

    /**
     * Charger uniquement les badges
     */
    fun loadBadges(filter: String = "earned") {
        viewModelScope.launch {
            _badges.value = Resource.Loading()
            val result = apprenantRepository.getBadges(filter)
            _badges.value = result
        }
    }

    /**
     * Charger uniquement les défis
     */
    fun loadChallenges() {
        viewModelScope.launch {
            _challenges.value = Resource.Loading()
            val result = apprenantRepository.getChallenges()
            _challenges.value = result
        }
    }

    /**
     * Charger uniquement le classement
     */
    fun loadLeaderboard() {
        viewModelScope.launch {
            _leaderboard.value = Resource.Loading()
            val result = apprenantRepository.getLeaderboard()
            _leaderboard.value = result
        }
    }

    /**
     * Rafraîchir toutes les données
     */
    fun refresh() {
        loadDashboard()
    }

    /**
     * Marquer un badge comme vu
     */
    fun markBadgeAsViewed(badgeId: Long) {
        viewModelScope.launch {
            apprenantRepository.markBadgeAsViewed(badgeId)
        }
    }

    /**
     * Marquer un défi comme vu
     */
    fun markChallengeAsViewed(challengeId: Long) {
        viewModelScope.launch {
            apprenantRepository.markChallengeAsViewed(challengeId)
        }
    }
}
