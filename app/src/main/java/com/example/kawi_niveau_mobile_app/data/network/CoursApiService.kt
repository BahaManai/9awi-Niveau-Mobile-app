package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.responses.CoursResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface CoursApiService {
    @GET("/api/cours")
    suspend fun getAllCours(@Header("Authorization") token: String): Response<List<CoursResponse>>

    @GET("/api/cours/{id}")
    suspend fun getCoursById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<CoursResponse>
}
