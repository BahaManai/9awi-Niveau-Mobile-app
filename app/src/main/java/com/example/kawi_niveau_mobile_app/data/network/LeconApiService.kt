package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.responses.LeconResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface LeconApiService {
    @GET("/api/lecons/module/{moduleId}")
    suspend fun getLeconsByModule(
        @Header("Authorization") token: String,
        @Path("moduleId") moduleId: Long
    ): Response<List<LeconResponse>>

    @GET("/api/lecons/{id}")
    suspend fun getLeconById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<LeconResponse>
}
