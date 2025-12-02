package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.responses.ModuleProgressResponse
import com.example.kawi_niveau_mobile_app.data.responses.ModuleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ModuleApiService {
    @GET("/api/modules/cours/{coursId}")
    suspend fun getModulesByCours(
        @Header("Authorization") token: String,
        @Path("coursId") coursId: Long
    ): Response<List<ModuleResponse>>

    @GET("/api/modules/{id}")
    suspend fun getModuleById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<ModuleResponse>

    @GET("/api/module-progress/cours/{coursId}")
    suspend fun getModulesWithProgress(
        @Header("Authorization") token: String,
        @Path("coursId") coursId: Long
    ): Response<List<ModuleProgressResponse>>
}
