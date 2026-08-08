package com.example.lxquiz.data.remote

import com.example.lxquiz.data.models.*
import com.example.lxquiz.domain.repository.CompetitionResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LxquizApiService {
    // User & Profile
    @POST("api/v1/users/")
    suspend fun createUserProfile(
        @Header("Authorization") token: String,
        @Body profile: UserProfile
    ): Response<UserProfile>

    @GET("api/v1/users/{uid}")
    suspend fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("uid") uid: String
    ): Response<UserProfile>

    @GET("api/v1/users/stats")
    suspend fun getUserStats(
        @Header("Authorization") token: String
    ): Response<UserStats>

    // Battles & Arena
    @POST("api/v1/battles/create")
    suspend fun createArenaRoom(
        @Header("Authorization") token: String,
        @Body config: ArenaConfig
    ): Response<ArenaRoom>

    @POST("api/v1/battles/join/{roomId}")
    suspend fun joinArenaRoom(
        @Header("Authorization") token: String,
        @Path("roomId") roomId: String
    ): Response<ArenaRoom>

    @GET("api/v1/battles/{roomId}")
    suspend fun getArenaStatus(
        @Header("Authorization") token: String,
        @Path("roomId") roomId: String
    ): Response<ArenaRoom>

    @POST("api/v1/battles/{roomId}/leave")
    suspend fun leaveArenaRoom(
        @Header("Authorization") token: String,
        @Path("roomId") roomId: String
    ): Response<Unit>

    @POST("api/v1/battles/{roomId}/ready")
    suspend fun toggleReady(
        @Header("Authorization") token: String,
        @Path("roomId") roomId: String,
        @Query("is_ready") isReady: Boolean
    ): Response<Unit>

    @POST("api/v1/battles/{roomId}/start")
    suspend fun startArena(
        @Header("Authorization") token: String,
        @Path("roomId") roomId: String
    ): Response<Unit>

    // Quizzes & Assessments
    @GET("api/v1/quizzes/generate")
    suspend fun generateQuiz(
        @Header("Authorization") token: String,
        @Query("subject_id") subjectId: String,
        @Query("count") count: Int
    ): Response<QuizSession>

    @POST("api/v1/quizzes/submit")
    suspend fun submitQuizResult(
        @Header("Authorization") token: String,
        @Body result: QuizResult
    ): Response<QuizResult>

    // Competitions & Tournaments
    @GET("api/v1/competitions/")
    suspend fun getTournaments(
        @Header("Authorization") token: String
    ): Response<List<Tournament>>

    @GET("api/v1/competitions/leaderboard")
    suspend fun getLeaderboard(
        @Header("Authorization") token: String,
        @Query("type") type: String, // global, continental, national
        @Query("region") region: String? = null
    ): Response<List<LeaderboardEntry>>

    // AI Assistant
    @POST("api/v1/assistant/chat")
    suspend fun sendMessageToAi(
        @Header("Authorization") token: String,
        @Body message: AiChatMessage
    ): Response<AiChatMessage>

    // LAC (Lxquiz Anti-Cheat) Security
    @POST("api/v1/security/report-event")
    suspend fun reportIntegrityEvent(
        @Header("Authorization") token: String,
        @Body event: IntegrityEventRequest
    ): Response<IntegrityResponse>

    // Competition Sync
    @POST("api/v1/competition/sync")
    suspend fun syncOfflineResults(
        @Header("Authorization") token: String,
        @Body results: List<CompetitionResult>
    ): Response<SyncResponse>
}
