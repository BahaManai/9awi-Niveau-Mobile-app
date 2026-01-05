package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.local.dao.UserDao
import com.example.kawi_niveau_mobile_app.data.requests.CoursRequest
import com.example.kawi_niveau_mobile_app.data.responses.CoursResponse
import com.example.kawi_niveau_mobile_app.data.responses.MessageResponse
import com.example.kawi_niveau_mobile_app.data.responses.UploadResponse
import com.example.kawi_niveau_mobile_app.data.network.CoursApiService
import com.example.kawi_niveau_mobile_app.data.network.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoursRepository @Inject constructor(
    private val coursApiService: CoursApiService,
    private val userDao: UserDao
) : BaseRepository() {
    
    suspend fun getAllCours(): Resource<List<CoursResponse>> {
        val token = userDao.getToken()
        android.util.Log.d("CoursRepository", "getAllCours - Token: ${if (token.isNullOrEmpty()) "EMPTY" else "Present (${token.length} chars)"}")
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            coursApiService.getAllCours("Bearer $token")
        }
    }
    
    suspend fun getMesCours(): Resource<List<CoursResponse>> {
        val token = userDao.getToken()
        android.util.Log.d("CoursRepository", "getMesCours - Token: ${if (token.isNullOrEmpty()) "EMPTY" else "Present (${token.length} chars)"}")
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            coursApiService.getMesCours("Bearer $token")
        }
    }
    
    suspend fun getCoursById(id: Long): Resource<CoursResponse> {
        val token = userDao.getToken()
        android.util.Log.d("CoursRepository", "getCoursById - Token: ${if (token.isNullOrEmpty()) "EMPTY" else "Present (${token.length} chars)"}")
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            coursApiService.getCoursById("Bearer $token", id)
        }
    }
    
    suspend fun createCours(request: CoursRequest): Resource<CoursResponse> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            coursApiService.createCours("Bearer $token", request)
        }
    }
    
    suspend fun updateCours(id: Long, request: CoursRequest): Resource<CoursResponse> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            coursApiService.updateCours("Bearer $token", id, request)
        }
    }
    
    suspend fun archiveCours(id: Long): Resource<MessageResponse> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            coursApiService.archiveCours("Bearer $token", id)
        }
    }
    
    suspend fun unarchiveCours(id: Long): Resource<MessageResponse> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            coursApiService.unarchiveCours("Bearer $token", id)
        }
    }
    
    suspend fun uploadThumbnail(file: File): Resource<UploadResponse> {
        val token = userDao.getToken()
        if (token.isNullOrEmpty()) return Resource.Error("Token manquant")
        return safeApiCall {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            coursApiService.uploadThumbnail("Bearer $token", body)
        }
    }
}
