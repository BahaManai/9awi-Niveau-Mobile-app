package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.dto.FormateurStatsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface FormateurApiService {
    
    @GET("api/mobile/formateur/stats")
    suspend fun getFormateurStats(
        @Header("Authorization") token: String
    ): Response<FormateurStatsResponse>
}