package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.local.dao.UserDao
import com.example.kawi_niveau_mobile_app.data.network.LeconApiService
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.responses.LeconResponse
import javax.inject.Inject

class LeconRepository @Inject constructor(
    private val leconApiService: LeconApiService,
    private val userDao: UserDao
) : BaseRepository() {

    suspend fun getLeconsByModule(moduleId: Long): Resource<List<LeconResponse>> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            leconApiService.getLeconsByModule("Bearer $token", moduleId)
        }
    }

    suspend fun getLeconById(id: Long): Resource<LeconResponse> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            leconApiService.getLeconById("Bearer $token", id)
        }
    }
}
