package com.example.kawi_niveau_mobile_app.data.network.dto

data class ApprenantDashboardResponse(
    val stats: UserStatsDto,
    val badges: List<BadgeDto>,
    val challenges: List<ChallengeDto>,
    val leaderboard: LeaderboardDto
)

data class UserStatsDto(
    val totalPoints: Int,
    val currentLevel: Int,
    val levelName: String,
    val levelDescription: String,
    val pointsToNextLevel: Int,
    val nextLevelPoints: Int,
    val progressPercent: Double,
    val badgesCount: Int,
    val completedChallenges: Int,
    val leaderboardPosition: Int
)

data class BadgeDto(
    val id: Long,
    val name: String,
    val description: String,
    val criteriaType: String,
    val iconUrl: String?,
    val earnedAt: Long,
    val isNew: Boolean
)

data class ChallengeDto(
    val id: Long,
    val name: String,
    val description: String,
    val challengeType: String,
    val targetValue: Int,
    val currentProgress: Int,
    val progressPercent: Double,
    val xpReward: Int,
    val isCompleted: Boolean,
    val completedAt: Long?,
    val joinedAt: Long?,
    val endDate: Long?,
    val timeRemaining: String?,
    val isNew: Boolean,
    val isActive: Boolean
)

data class LeaderboardDto(
    val userPosition: UserPositionDto,
    val topLeaderboard: List<LeaderboardEntryDto>
)

data class UserPositionDto(
    val rank: Int,
    val name: String,
    val totalPoints: Int,
    val level: Int,
    val levelName: String,
    val badgesCount: Int
)

data class LeaderboardEntryDto(
    val rank: Int,
    val name: String,
    val totalPoints: Int,
    val level: Int,
    val levelName: String,
    val badgesCount: Int,
    val isCurrentUser: Boolean
)
