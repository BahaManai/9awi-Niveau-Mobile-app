package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.dto.ParcoursResponse
import com.example.kawi_niveau_mobile_app.data.dto.ParcoursProgressionStatsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ParcoursApiService {
    
    @GET("api/mobile/formateur/parcours")
    suspend fun getParcoursFormateur(
        @Header("Authorization") token: String
    ): Response<List<ParcoursResponse>>
    
    @GET("api/mobile/parcours/{id}/progression")
    suspend fun getParcoursProgressionStats(
        @Path("id") parcoursId: Long,
        @Header("Authorization") token: String
    ): Response<ParcoursProgressionStatsResponse>
}