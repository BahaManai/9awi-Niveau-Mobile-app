package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.UserPreferences
import com.example.kawi_niveau_mobile_app.data.network.ModuleApiService
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.responses.ModuleProgressResponse
import com.example.kawi_niveau_mobile_app.data.responses.ModuleResponse
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ModuleRepository @Inject constructor(
    private val moduleApiService: ModuleApiService,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getModulesByCours(coursId: Long): Resource<List<ModuleResponse>> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            moduleApiService.getModulesByCours("Bearer $token", coursId)
        }
    }

    suspend fun getModuleById(id: Long): Resource<ModuleResponse> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            moduleApiService.getModuleById("Bearer $token", id)
        }
    }

    suspend fun getModulesWithProgress(coursId: Long): Resource<List<ModuleProgressResponse>> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            moduleApiService.getModulesWithProgress("Bearer $token", coursId)
        }
    }
}
