package com.example.lxquiz.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ArenaConfig(
    val subjectId: String,
    val subjectName: String,
    val level: EducationLevel,
    val subLevelId: String? = null,
    val subLevelName: String? = null,
    val sectionId: String? = null,
    val sectionName: String? = null,
    val questionCount: Int,
    val stakesMultiplier: Float = 1.0f
)

@Serializable
data class ArenaPlayer(
    val uid: String,
    val name: String,
    val countryId: String,
    val level: EducationLevel,
    val subLevelId: String,
    val photoUrl: String? = null,
    val isReady: Boolean = false,
    val isHost: Boolean = false,
    val score: Int = 0,
    val progress: Float = 0f
)

@Serializable
data class ArenaRoom(
    val roomId: String,
    val config: ArenaConfig,
    val players: List<ArenaPlayer>,
    val baseCurriculumPlayerId: String? = null, // The player whose curriculum is used for quiz generation
    val status: ArenaStatus = ArenaStatus.WAITING,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ArenaStatus {
    WAITING,
    STARTING,
    IN_PROGRESS,
    COMPLETED
}
