package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.local.dao.UserDao
import com.example.kawi_niveau_mobile_app.data.network.CoursApiService
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.responses.CoursResponse
import javax.inject.Inject

class CoursRepository @Inject constructor(
    private val coursApiService: CoursApiService,
    private val userDao: UserDao
) : BaseRepository() {

    suspend fun getAllCours(): Resource<List<CoursResponse>> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            coursApiService.getAllCours("Bearer $token")
        }
    }

    suspend fun getCoursById(id: Long): Resource<CoursResponse> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            coursApiService.getCoursById("Bearer $token", id)
        }
    }
}
