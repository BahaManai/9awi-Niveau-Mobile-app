package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.local.dao.UserDao
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userDao: UserDao
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            userDao.getToken()
        }
        
        android.util.Log.d("AuthInterceptor", "Token: ${token?.take(20)}...")
        android.util.Log.d("AuthInterceptor", "URL: ${chain.request().url}")
        
        val request = if (token != null && token.isNotEmpty()) {
            android.util.Log.d("AuthInterceptor", "Adding Authorization header")
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            android.util.Log.e("AuthInterceptor", "No token found!")
            chain.request()
        }
        
        val response = chain.proceed(request)
        android.util.Log.d("AuthInterceptor", "Response code: ${response.code}")
        
        return response
    }
}
