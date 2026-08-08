package com.example.lxquiz.data.repository

import com.example.lxquiz.data.local.dao.CompetitionDao
import com.example.lxquiz.data.local.entities.CompetitionResultEntity
import com.example.lxquiz.data.models.*
import com.example.lxquiz.data.remote.LxquizApiService
import com.example.lxquiz.data.remote.NetworkClient
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.repository.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompetitionRepository @Inject constructor(
    private val apiService: LxquizApiService,
    private val competitionDao: CompetitionDao,
    private val authRepository: IAuthRepository
) : ICompetitionRepository {
    
    override suspend fun getTournaments(): NetworkResult<List<Tournament>> {
        delay(800)
        return NetworkResult.Success(
            listOf(
                Tournament(id = "1", title = "Science Olympiad", status = TournamentStatus.ONGOING),
                Tournament(id = "2", title = "Math Whiz", status = TournamentStatus.UPCOMING)
            )
        )
    }

    override suspend fun getLeaderboard(type: String, region: String?): NetworkResult<List<LeaderboardEntry>> {
        delay(800)
        return NetworkResult.Success(
            listOf(
                LeaderboardEntry("1", "Alex Ngar", 1, 15420, "cm"),
                LeaderboardEntry("2", "Sarah Smith", 2, 14200, "us")
            )
        )
    }

    override fun observeMatchUpdates(matchId: String): Flow<MatchUpdate> = flow {
        // Real-time implementation via WebSockets would go here
        emit(MatchUpdate.PlayerScore("1", 100))
        delay(2000)
        emit(MatchUpdate.PlayerScore("2", 150))
    }

    override suspend fun sendMatchAction(matchId: String, action: MatchAction): NetworkResult<Unit> {
        // Send action via WebSocket or REST
        return NetworkResult.Success(Unit)
    }

    override suspend fun saveResultLocally(result: CompetitionResult) {
        val userId = authRepository.currentUserProfile?.uid ?: "anonymous"
        competitionDao.insertResult(
            CompetitionResultEntity(
                userId = userId,
                tournamentId = result.tournamentId,
                matchId = result.matchId,
                score = result.score,
                correctAnswers = result.correctAnswers,
                totalQuestions = result.totalQuestions,
                timeTakenSeconds = result.timeTakenSeconds
            )
        )
    }

    override suspend fun syncOfflineResults(): NetworkResult<Int> {
        val unsynced = competitionDao.getUnsyncedResults()
        if (unsynced.isEmpty()) return NetworkResult.Success(0)

        return try {
            val results = unsynced.map { 
                CompetitionResult(it.tournamentId, it.matchId, it.score, it.correctAnswers, it.totalQuestions, it.timeTakenSeconds)
            }
            
            val response = apiService.syncOfflineResults("Bearer DEBUG", results)
            if (response.isSuccessful) {
                competitionDao.markAsSynced(unsynced.map { it.id })
                NetworkResult.Success(response.body()?.syncedCount ?: 0)
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    override fun observeTotalScore(): Flow<Int> {
        val userId = authRepository.currentUserProfile?.uid ?: ""
        return competitionDao.getTotalOfflineScore(userId).map { it ?: 0 }
    }
}
