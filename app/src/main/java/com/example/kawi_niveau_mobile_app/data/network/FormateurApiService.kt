package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.dto.FormateurStatsResponse
import com.example.kawi_niveau_mobile_app.data.dto.CoursStatsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface FormateurApiService {
    
    @GET("api/mobile/formateur/stats")
    suspend fun getFormateurStats(
        @Header("Authorization") token: String
    ): Response<FormateurStatsResponse>
    
    @GET("api/mobile/cours/{id}/stats")
    suspend fun getCoursStats(
        @Header("Authorization") token: String,
        @Path("id") coursId: Long
    ): Response<CoursStatsResponse>
}