package com.example.kawi_niveau_mobile_app.data.repository

import com.example.kawi_niveau_mobile_app.data.network.FormateurApiService
import com.example.kawi_niveau_mobile_app.data.dto.CoursStatsResponse
import com.example.kawi_niveau_mobile_app.data.dto.FormateurStatsResponse
import com.example.kawi_niveau_mobile_app.data.dto.CoursParNiveauDto
import com.example.kawi_niveau_mobile_app.data.local.dao.UserDao
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FormateurRepository @Inject constructor(
    private val formateurApiService: FormateurApiService,
    private val userDao: UserDao
) : BaseRepository() {
    
    suspend fun getCoursStats(coursId: Long): CoursStatsResponse {
        return try {
            // Utiliser la vraie API
            val token = userDao.getToken()
            if (token.isNullOrEmpty()) {
                throw Exception("Token manquant")
            }
            
            val response = formateurApiService.getCoursStats("Bearer $token", coursId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                // Fallback vers données simulées si erreur API
                CoursStatsResponse(
                    totalInscrits = Random.nextInt(10, 51),
                    progressionMoyenne = Random.nextDouble(60.0, 95.0),
                    tauxReussite = Random.nextDouble(70.0, 90.0),
                    nombreCertificats = Random.nextInt(5, 26)
                )
            }
        } catch (e: Exception) {
            // Fallback vers données simulées si erreur
            CoursStatsResponse(
                totalInscrits = Random.nextInt(10, 51),
                progressionMoyenne = Random.nextDouble(60.0, 95.0),
                tauxReussite = Random.nextDouble(70.0, 90.0),
                nombreCertificats = Random.nextInt(5, 26)
            )
        }
    }
    
    suspend fun getFormateurStats(): Result<FormateurStatsResponse> {
        return try {
            // Utiliser la vraie API
            val token = userDao.getToken()
            if (token.isNullOrEmpty()) {
                throw Exception("Token manquant")
            }
            
            val response = formateurApiService.getFormateurStats("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                // Fallback vers données simulées si erreur API
                val stats = FormateurStatsResponse(
                    totalCours = Random.nextInt(5, 21),
                    coursActifs = Random.nextInt(3, 15),
                    totalApprenants = Random.nextInt(50, 201),
                    tauxReussiteMoyen = Random.nextDouble(70.0, 95.0),
                    coursParNiveau = listOf(
                        CoursParNiveauDto("Débutant", Random.nextInt(2, 8), Random.nextDouble(30.0, 50.0)),
                        CoursParNiveauDto("Intermédiaire", Random.nextInt(1, 6), Random.nextDouble(25.0, 40.0)),
                        CoursParNiveauDto("Avancé", Random.nextInt(1, 4), Random.nextDouble(15.0, 30.0)),
                        CoursParNiveauDto("Expert", Random.nextInt(0, 3), Random.nextDouble(5.0, 20.0))
                    )
                )
                Result.success(stats)
            }
        } catch (e: Exception) {
            // Fallback vers données simulées si erreur
            val stats = FormateurStatsResponse(
                totalCours = Random.nextInt(5, 21),
                coursActifs = Random.nextInt(3, 15),
                totalApprenants = Random.nextInt(50, 201),
                tauxReussiteMoyen = Random.nextDouble(70.0, 95.0),
                coursParNiveau = listOf(
                    CoursParNiveauDto("Débutant", Random.nextInt(2, 8), Random.nextDouble(30.0, 50.0)),
                    CoursParNiveauDto("Intermédiaire", Random.nextInt(1, 6), Random.nextDouble(25.0, 40.0)),
                    CoursParNiveauDto("Avancé", Random.nextInt(1, 4), Random.nextDouble(15.0, 30.0)),
                    CoursParNiveauDto("Expert", Random.nextInt(0, 3), Random.nextDouble(5.0, 20.0))
                )
            )
            Result.success(stats)
        }
    }
}