package com.example.lxquiz.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserStats(
    val totalQuizzesTaken: Int = 0,
    val averageScore: Float = 0f,
    val totalTimeSpentHours: Float = 0f,
    val streakDays: Int = 0,
    val rankGlobal: Int = 0,
    val rankNational: Int = 0,
    val subjectPerformance: Map<String, Float> = emptyMap(),
    val scoreTrend: List<ScorePoint> = emptyList()
)

@Serializable
data class ScorePoint(
    val date: Long,
    val score: Int
)

@Serializable
data class AiLearningTip(
    val title: String,
    val content: String,
    val category: String
)
