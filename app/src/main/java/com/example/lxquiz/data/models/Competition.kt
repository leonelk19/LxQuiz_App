package com.example.lxquiz.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Tournament(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val icon: String = "EmojiEvents",
    val colorHex: String = "#00C853",
    val startTime: Long = 0,
    val endTime: Long = 0,
    val participantCount: Int = 0,
    val rewardXp: Int = 0,
    val rewardBadge: String? = null,
    val status: TournamentStatus = TournamentStatus.UPCOMING
)

enum class TournamentStatus {
    UPCOMING, ONGOING, COMPLETED
}

@Serializable
data class LeaderboardEntry(
    val userId: String,
    val userName: String,
    val rank: Int,
    val points: Int,
    val countryId: String,
    val photoUrl: String? = null
)

@Serializable
data class SyncResponse(
    val syncedCount: Int,
    val totalPointsEarned: Int
)
