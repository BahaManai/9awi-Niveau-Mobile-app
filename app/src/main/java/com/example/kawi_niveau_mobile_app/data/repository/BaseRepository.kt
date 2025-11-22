package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.responses.ErrorResponse
import com.google.gson.Gson
import retrofit2.Response

abstract class BaseRepository {
    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>
    ): Resource<T> {
        return try {
            android.util.Log.d("BaseRepository", "Making API call")
            val response = apiCall()
            android.util.Log.d("BaseRepository", "Response code: ${response.code()}")
            if (response.isSuccessful) {
                android.util.Log.d("BaseRepository", "API call successful")
                Resource.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("BaseRepository", "API error - Code: ${response.code()}, Body: $errorBody")
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                Resource.Error(errorResponse.message ?: response.message())
            }
        } catch (e: Exception) {
            android.util.Log.e("BaseRepository", "Exception during API call: ${e.message}", e)
            Resource.Error(e.message ?: "Unknown error")
        }
    }
}