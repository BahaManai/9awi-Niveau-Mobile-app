package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.dto.ParcoursResponse
import com.example.kawi_niveau_mobile_app.data.dto.ParcoursProgressionStatsResponse
import com.example.kawi_niveau_mobile_app.data.network.ParcoursApiService
import com.example.kawi_niveau_mobile_app.data.local.dao.UserDao
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParcoursRepository @Inject constructor(
    private val parcoursApiService: ParcoursApiService,
    private val userDao: UserDao
) {
    
    suspend fun getParcoursFormateur(): Result<List<ParcoursResponse>> {
        return try {
            val user = userDao.getUser()
            val token = "Bearer ${user?.token}"
            
            val response = parcoursApiService.getParcoursFormateur(token)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erreur lors de la récupération des parcours"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getParcoursProgressionStats(parcoursId: Long): Result<ParcoursProgressionStatsResponse> {
        return try {
            val user = userDao.getUser()
            val token = "Bearer ${user?.token}"
            
            val response = parcoursApiService.getParcoursProgressionStats(parcoursId, token)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erreur lors de la récupération des statistiques de progression"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}