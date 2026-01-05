package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.requests.CoursRequest
import com.example.kawi_niveau_mobile_app.data.responses.CoursResponse
import com.example.kawi_niveau_mobile_app.data.responses.MessageResponse
import com.example.kawi_niveau_mobile_app.data.responses.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface CoursApiService {
    
    @GET("api/cours")
    suspend fun getAllCours(
        @Header("Authorization") token: String
    ): Response<List<CoursResponse>>
    
    @GET("api/cours/mes-cours")
    suspend fun getMesCours(
        @Header("Authorization") token: String
    ): Response<List<CoursResponse>>
    
    @GET("api/cours/{id}")
    suspend fun getCoursById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<CoursResponse>
    
    @POST("api/cours")
    suspend fun createCours(
        @Header("Authorization") token: String,
        @Body request: CoursRequest
    ): Response<CoursResponse>
    
    @PUT("api/cours/{id}")
    suspend fun updateCours(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: CoursRequest
    ): Response<CoursResponse>
    
    @PUT("api/cours/{id}/archive")
    suspend fun archiveCours(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<MessageResponse>
    
    @PUT("api/cours/{id}/unarchive")
    suspend fun unarchiveCours(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<MessageResponse>
    
    @Multipart
    @POST("images/cours/upload")
    suspend fun uploadThumbnail(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Response<UploadResponse>
}
