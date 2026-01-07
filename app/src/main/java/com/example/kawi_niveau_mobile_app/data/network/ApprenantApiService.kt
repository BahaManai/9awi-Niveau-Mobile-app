package com.example.kawi_niveau_mobile_app.data.network

import com.example.kawi_niveau_mobile_app.data.network.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApprenantApiService {
    
    /**
     * Récupérer le dashboard complet de l'apprenant
     */
    @GET("api/mobile/apprenant/dashboard")
    suspend fun getDashboard(
        @Header("Authorization") token: String
    ): Response<ApprenantDashboardResponse>
    
    /**
     * Récupérer les statistiques de l'utilisateur
     */
    @GET("api/mobile/apprenant/stats")
    suspend fun getStats(
        @Header("Authorization") token: String
    ): Response<UserStatsDto>
    
    /**
     * Récupérer les badges de l'utilisateur
     * @param filter Options: "all", "earned", "locked", "new"
     */
    @GET("api/mobile/apprenant/badges")
    suspend fun getBadges(
        @Header("Authorization") token: String,
        @Query("filter") filter: String = "earned"
    ): Response<List<BadgeDto>>
    
    /**
     * Récupérer les défis de l'utilisateur
     */
    @GET("api/mobile/apprenant/challenges")
    suspend fun getChallenges(
        @Header("Authorization") token: String
    ): Response<List<ChallengeDto>>
    
    /**
     * Récupérer le classement
     */
    @GET("api/mobile/apprenant/leaderboard")
    suspend fun getLeaderboard(
        @Header("Authorization") token: String
    ): Response<LeaderboardDto>
    
    /**
     * Marquer un badge comme vu
     */
    @POST("api/mobile/apprenant/badges/{badgeId}/view")
    suspend fun markBadgeAsViewed(
        @Header("Authorization") token: String,
        @Path("badgeId") badgeId: Long
    ): Response<Map<String, String>>
    
    /**
     * Marquer un défi comme vu
     */
    @POST("api/mobile/apprenant/challenges/{challengeId}/view")
    suspend fun markChallengeAsViewed(
        @Header("Authorization") token: String,
        @Path("challengeId") challengeId: Long
    ): Response<Map<String, String>>
}
