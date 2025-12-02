package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.requests.EnrollmentRequest
import com.example.kawi_niveau_mobile_app.data.requests.LeconCompletionRequest
import com.example.kawi_niveau_mobile_app.data.responses.EnrollmentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface EnrollmentApiService {
    @POST("/api/enrollments")
    suspend fun enrollInCourse(
        @Header("Authorization") token: String,
        @Body request: EnrollmentRequest
    ): Response<EnrollmentResponse>

    @GET("/api/enrollments")
    suspend fun getUserEnrollments(@Header("Authorization") token: String): Response<List<EnrollmentResponse>>

    @GET("/api/enrollments/cours/{coursId}")
    suspend fun getEnrollmentDetails(
        @Header("Authorization") token: String,
        @Path("coursId") coursId: Long
    ): Response<EnrollmentResponse>

    @POST("/api/enrollments/cours/{coursId}/complete-lecon")
    suspend fun markLeconAsCompleted(
        @Header("Authorization") token: String,
        @Path("coursId") coursId: Long,
        @Body request: LeconCompletionRequest
    ): Response<EnrollmentResponse>

    @DELETE("/api/enrollments/cours/{coursId}/lecons/{leconId}/completion")
    suspend fun unmarkLeconAsCompleted(
        @Header("Authorization") token: String,
        @Path("coursId") coursId: Long,
        @Path("leconId") leconId: Long
    ): Response<EnrollmentResponse>

    @GET("/api/enrollments/cours/{coursId}/completed-lecons")
    suspend fun getCompletedLeconIds(
        @Header("Authorization") token: String,
        @Path("coursId") coursId: Long
    ): Response<List<Long>>
}
