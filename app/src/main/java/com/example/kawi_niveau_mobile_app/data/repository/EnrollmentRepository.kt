package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.UserPreferences
import com.example.kawi_niveau_mobile_app.data.network.EnrollmentApiService
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.requests.EnrollmentRequest
import com.example.kawi_niveau_mobile_app.data.requests.LeconCompletionRequest
import com.example.kawi_niveau_mobile_app.data.responses.EnrollmentResponse
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class EnrollmentRepository @Inject constructor(
    private val enrollmentApiService: EnrollmentApiService,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun enrollInCourse(coursId: Long): Resource<EnrollmentResponse> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            enrollmentApiService.enrollInCourse("Bearer $token", EnrollmentRequest(coursId))
        }
    }

    suspend fun getUserEnrollments(): Resource<List<EnrollmentResponse>> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            enrollmentApiService.getUserEnrollments("Bearer $token")
        }
    }

    suspend fun getEnrollmentDetails(coursId: Long): Resource<EnrollmentResponse> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            enrollmentApiService.getEnrollmentDetails("Bearer $token", coursId)
        }
    }

    suspend fun markLeconAsCompleted(coursId: Long, leconId: Long): Resource<EnrollmentResponse> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            enrollmentApiService.markLeconAsCompleted("Bearer $token", coursId, LeconCompletionRequest(leconId))
        }
    }

    suspend fun unmarkLeconAsCompleted(coursId: Long, leconId: Long): Resource<EnrollmentResponse> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            enrollmentApiService.unmarkLeconAsCompleted("Bearer $token", coursId, leconId)
        }
    }

    suspend fun getCompletedLeconIds(coursId: Long): Resource<List<Long>> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            enrollmentApiService.getCompletedLeconIds("Bearer $token", coursId)
        }
    }
}
