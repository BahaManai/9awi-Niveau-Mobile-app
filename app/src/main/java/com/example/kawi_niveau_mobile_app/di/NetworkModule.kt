package com.example.kawi_niveau_mobile_app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.example.kawi_niveau_mobile_app.BuildConfig
import com.example.kawi_niveau_mobile_app.data.local.AppDatabase
import com.example.kawi_niveau_mobile_app.data.local.dao.UserDao
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(userDao: UserDao): com.example.kawi_niveau_mobile_app.data.network.AuthInterceptor {
        return com.example.kawi_niveau_mobile_app.data.network.AuthInterceptor(userDao)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        authInterceptor: com.example.kawi_niveau_mobile_app.data.network.AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): com.example.kawi_niveau_mobile_app.data.network.AuthApiService {
        return retrofit.create(com.example.kawi_niveau_mobile_app.data.network.AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): com.example.kawi_niveau_mobile_app.data.network.ProfileApiService {
        return retrofit.create(com.example.kawi_niveau_mobile_app.data.network.ProfileApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCoursApiService(retrofit: Retrofit): com.example.kawi_niveau_mobile_app.data.network.CoursApiService {
        return retrofit.create(com.example.kawi_niveau_mobile_app.data.network.CoursApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideEnrollmentApiService(retrofit: Retrofit): com.example.kawi_niveau_mobile_app.data.network.EnrollmentApiService {
        return retrofit.create(com.example.kawi_niveau_mobile_app.data.network.EnrollmentApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideModuleApiService(retrofit: Retrofit): com.example.kawi_niveau_mobile_app.data.network.ModuleApiService {
        return retrofit.create(com.example.kawi_niveau_mobile_app.data.network.ModuleApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLeconApiService(retrofit: Retrofit): com.example.kawi_niveau_mobile_app.data.network.LeconApiService {
        return retrofit.create(com.example.kawi_niveau_mobile_app.data.network.LeconApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideQuizApiService(retrofit: Retrofit): com.example.kawi_niveau_mobile_app.data.network.QuizApiService {
        return retrofit.create(com.example.kawi_niveau_mobile_app.data.network.QuizApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { appContext.preferencesDataStoreFile("user_prefs") }
        )
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "kawi_niveau_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
}
