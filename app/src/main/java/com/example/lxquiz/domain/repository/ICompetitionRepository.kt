package com.example.lxquiz.domain.repository

import com.example.lxquiz.data.models.LeaderboardEntry
import com.example.lxquiz.data.models.Tournament
import com.example.lxquiz.data.remote.NetworkResult
import kotlinx.coroutines.flow.Flow

interface ICompetitionRepository {
    // REST
    suspend fun getTournaments(): NetworkResult<List<Tournament>>
    suspend fun getLeaderboard(type: String, region: String? = null): NetworkResult<List<LeaderboardEntry>>
    
    // Real-time (WebSockets)
    fun observeMatchUpdates(matchId: String): Flow<MatchUpdate>
    suspend fun sendMatchAction(matchId: String, action: MatchAction): NetworkResult<Unit>
    
    // Offline / Sync
    suspend fun saveResultLocally(result: CompetitionResult): Unit
    suspend fun syncOfflineResults(): NetworkResult<Int> // Returns number of synced results
    fun observeTotalScore(): Flow<Int>
}

sealed class MatchUpdate {
    data class PlayerScore(val userId: String, val score: Int) : MatchUpdate()
    data class MatchEnded(val winnerId: String, val finalScores: Map<String, Int>) : MatchUpdate()
    data class Error(val message: String) : MatchUpdate()
}

sealed class MatchAction {
    data class SubmitAnswer(val questionId: String, val optionIndex: Int) : MatchAction()
    object LeaveMatch : MatchAction()
}

data class CompetitionResult(
    val tournamentId: String?,
    val matchId: String?,
    val score: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val timeTakenSeconds: Int
)
