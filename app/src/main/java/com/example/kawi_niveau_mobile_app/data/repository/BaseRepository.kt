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
            val response = apiCall()
            if (response.isSuccessful) {
                Resource.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                Resource.Error(errorResponse.message ?: response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }
}