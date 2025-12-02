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
    suspend fun getAllCours(): Response<List<CoursResponse>>
    
    @GET("api/cours/mes-cours")
    suspend fun getMesCours(): Response<List<CoursResponse>>
    
    @GET("api/cours/{id}")
    suspend fun getCoursById(@Path("id") id: Long): Response<CoursResponse>
    
    @POST("api/cours")
    suspend fun createCours(@Body request: CoursRequest): Response<CoursResponse>
    
    @PUT("api/cours/{id}")
    suspend fun updateCours(
        @Path("id") id: Long,
        @Body request: CoursRequest
    ): Response<CoursResponse>
    
    @PUT("api/cours/{id}/archive")
    suspend fun archiveCours(@Path("id") id: Long): Response<MessageResponse>
    
    @PUT("api/cours/{id}/unarchive")
    suspend fun unarchiveCours(@Path("id") id: Long): Response<MessageResponse>
    
    @Multipart
    @POST("images/cours/upload")
    suspend fun uploadThumbnail(
        @Part file: MultipartBody.Part
    ): Response<UploadResponse>
}
