package com.example.lxquiz.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_competition_results")
data class CompetitionResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val tournamentId: String?,
    val matchId: String?,
    val score: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val timeTakenSeconds: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)
