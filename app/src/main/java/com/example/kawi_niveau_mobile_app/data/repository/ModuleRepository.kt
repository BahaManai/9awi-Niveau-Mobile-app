package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.local.dao.UserDao
import com.example.kawi_niveau_mobile_app.data.network.ModuleApiService
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.responses.ModuleProgressResponse
import com.example.kawi_niveau_mobile_app.data.responses.ModuleResponse
import javax.inject.Inject

class ModuleRepository @Inject constructor(
    private val moduleApiService: ModuleApiService,
    private val userDao: UserDao
) : BaseRepository() {

    suspend fun getModulesByCours(coursId: Long): Resource<List<ModuleResponse>> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            moduleApiService.getModulesByCours("Bearer $token", coursId)
        }
    }

    suspend fun getModuleById(id: Long): Resource<ModuleResponse> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            moduleApiService.getModuleById("Bearer $token", id)
        }
    }

    suspend fun getModulesWithProgress(coursId: Long): Resource<List<ModuleProgressResponse>> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            moduleApiService.getModulesWithProgress("Bearer $token", coursId)
        }
    }
}
