package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.dto.FormateurStatsResponse
import com.example.kawi_niveau_mobile_app.data.network.FormateurApiService
import com.example.kawi_niveau_mobile_app.data.local.dao.UserDao
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormateurRepository @Inject constructor(
    private val formateurApiService: FormateurApiService,
    private val userDao: UserDao
) {
    
    suspend fun getFormateurStats(): Result<FormateurStatsResponse> {
        return try {
            val user = userDao.getUser()
            val token = "Bearer ${user?.token}"
            
            val response = formateurApiService.getFormateurStats(token)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erreur lors de la récupération des statistiques"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}