package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.local.dao.UserDao
import com.example.kawi_niveau_mobile_app.data.network.ApprenantApiService
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.network.dto.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApprenantRepository @Inject constructor(
    private val apiService: ApprenantApiService,
    private val userDao: UserDao
) {

    /**
     * Récupérer le dashboard complet de l'apprenant
     */
    suspend fun getDashboard(): Resource<ApprenantDashboardResponse> = withContext(Dispatchers.IO) {
        try {
            val token = getAuthToken()
            val response = apiService.getDashboard(token)
            
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Erreur: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Erreur réseau: ${e.localizedMessage}")
        }
    }

    /**
     * Récupérer les statistiques de l'utilisateur
     */
    suspend fun getStats(): Resource<UserStatsDto> = withContext(Dispatchers.IO) {
        try {
            val token = getAuthToken()
            val response = apiService.getStats(token)
            
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Erreur: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Erreur réseau: ${e.localizedMessage}")
        }
    }

    /**
     * Récupérer les badges de l'utilisateur
     */
    suspend fun getBadges(filter: String = "earned"): Resource<List<BadgeDto>> = withContext(Dispatchers.IO) {
        try {
            val token = getAuthToken()
            val response = apiService.getBadges(token, filter)
            
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Erreur: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Erreur réseau: ${e.localizedMessage}")
        }
    }

    /**
     * Récupérer les défis de l'utilisateur
     */
    suspend fun getChallenges(): Resource<List<ChallengeDto>> = withContext(Dispatchers.IO) {
        try {
            val token = getAuthToken()
            val response = apiService.getChallenges(token)
            
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Erreur: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Erreur réseau: ${e.localizedMessage}")
        }
    }

    /**
     * Récupérer le classement
     */
    suspend fun getLeaderboard(): Resource<LeaderboardDto> = withContext(Dispatchers.IO) {
        try {
            val token = getAuthToken()
            val response = apiService.getLeaderboard(token)
            
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Erreur: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Erreur réseau: ${e.localizedMessage}")
        }
    }

    /**
     * Marquer un badge comme vu
     */
    suspend fun markBadgeAsViewed(badgeId: Long): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            val token = getAuthToken()
            val response = apiService.markBadgeAsViewed(token, badgeId)
            
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Erreur: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Erreur réseau: ${e.localizedMessage}")
        }
    }

    /**
     * Marquer un défi comme vu
     */
    suspend fun markChallengeAsViewed(challengeId: Long): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            val token = getAuthToken()
            val response = apiService.markChallengeAsViewed(token, challengeId)
            
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Erreur: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Erreur réseau: ${e.localizedMessage}")
        }
    }

    private suspend fun getAuthToken(): String {
        val user = userDao.getUser()
        return "Bearer ${user?.token ?: ""}"
    }
}
