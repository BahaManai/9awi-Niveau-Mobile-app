package com.example.kawi_niveau_mobile_app.data.repository

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
    private val coursApiService: CoursApiService
) {
    
    suspend fun getAllCours(): Resource<List<CoursResponse>> {
        return try {
            val response = coursApiService.getAllCours()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message() ?: "Erreur lors du chargement des cours")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erreur réseau")
        }
    }
    
    suspend fun getMesCours(): Resource<List<CoursResponse>> {
        return try {
            android.util.Log.d("CoursRepository", "Calling getMesCours API...")
            val response = coursApiService.getMesCours()
            android.util.Log.d("CoursRepository", "Response code: ${response.code()}")
            if (response.isSuccessful && response.body() != null) {
                android.util.Log.d("CoursRepository", "Success: ${response.body()!!.size} cours")
                Resource.Success(response.body()!!)
            } else {
                android.util.Log.e("CoursRepository", "Error: ${response.message()}")
                Resource.Error(response.message() ?: "Erreur lors du chargement de vos cours")
            }
        } catch (e: Exception) {
            android.util.Log.e("CoursRepository", "Exception: ${e.message}", e)
            Resource.Error(e.message ?: "Erreur réseau")
        }
    }
    
    suspend fun getCoursById(id: Long): Resource<CoursResponse> {
        return try {
            val response = coursApiService.getCoursById(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message() ?: "Cours non trouvé")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erreur réseau")
        }
    }
    
    suspend fun createCours(request: CoursRequest): Resource<CoursResponse> {
        return try {
            val response = coursApiService.createCours(request)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message() ?: "Erreur lors de la création du cours")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erreur réseau")
        }
    }
    
    suspend fun updateCours(id: Long, request: CoursRequest): Resource<CoursResponse> {
        return try {
            val response = coursApiService.updateCours(id, request)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message() ?: "Erreur lors de la modification du cours")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erreur réseau")
        }
    }
    
    suspend fun archiveCours(id: Long): Resource<MessageResponse> {
        return try {
            val response = coursApiService.archiveCours(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message() ?: "Erreur lors de l'archivage")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erreur réseau")
        }
    }
    
    suspend fun unarchiveCours(id: Long): Resource<MessageResponse> {
        return try {
            val response = coursApiService.unarchiveCours(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message() ?: "Erreur lors de la réactivation")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erreur réseau")
        }
    }
    
    suspend fun uploadThumbnail(file: File): Resource<UploadResponse> {
        return try {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            
            val response = coursApiService.uploadThumbnail(body)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message() ?: "Erreur lors de l'upload")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erreur réseau")
        }
    }
}
