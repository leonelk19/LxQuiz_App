package com.example.lxquiz.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: String = "",
    val text: String = "",
    val options: List<String> = emptyList(),
    val correctAnswerIndex: Int = 0,
    val explanation: String = "",
    val subjectId: String = "",
    val difficulty: String = "Medium",
    val tags: List<String> = emptyList()
)

@Serializable
data class QuizSession(
    val sessionId: String = "",
    val subjectId: String = "",
    val questions: List<Question> = emptyList(),
    val startTime: Long = System.currentTimeMillis(),
    val timeLimitSeconds: Int = 0
)

@Serializable
data class QuizResult(
    val sessionId: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val timeTakenSeconds: Int = 0,
    val completedAt: Long = System.currentTimeMillis(),
    val xpEarned: Int = 0,
    val feedback: String = ""
)
