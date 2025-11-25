package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.UserPreferences
import com.example.kawi_niveau_mobile_app.data.network.RemoteDataSource
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.requests.EnrollmentRequest
import com.example.kawi_niveau_mobile_app.data.responses.EnrollmentResponse
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class EnrollmentRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun enrollInCourse(coursId: Long): Resource<EnrollmentResponse> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            remoteDataSource.enrollInCourse(token, EnrollmentRequest(coursId))
        }
    }

    suspend fun getUserEnrollments(): Resource<List<EnrollmentResponse>> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            remoteDataSource.getUserEnrollments(token)
        }
    }

    suspend fun getEnrollmentDetails(coursId: Long): Resource<EnrollmentResponse> {
        val token = userPreferences.getToken().first()
        if (token.isEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            remoteDataSource.getEnrollmentDetails(token, coursId)
        }
    }
}
