package com.example.lxquiz.domain.repository

import com.example.lxquiz.data.models.ArenaConfig
import com.example.lxquiz.data.models.ArenaRoom
import com.example.lxquiz.data.remote.NetworkResult
import kotlinx.coroutines.flow.Flow

interface IArenaRepository {
    suspend fun createRoom(config: ArenaConfig): NetworkResult<ArenaRoom>
    suspend fun joinRoom(roomId: String): NetworkResult<ArenaRoom>
    suspend fun leaveRoom(roomId: String): NetworkResult<Unit>
    suspend fun toggleReady(roomId: String, isReady: Boolean): NetworkResult<Unit>
    suspend fun startArena(roomId: String): NetworkResult<Unit>
    
    /**
     * Observes real-time updates of the arena room (players joining, ready status, match starting).
     */
    fun observeRoomUpdates(roomId: String): Flow<ArenaRoomUpdate>
}

sealed class ArenaRoomUpdate {
    data class RoomState(val room: ArenaRoom) : ArenaRoomUpdate()
    data class PlayerJoined(val playerName: String) : ArenaRoomUpdate()
    data class PlayerLeft(val playerName: String) : ArenaRoomUpdate()
    object MatchStarting : ArenaRoomUpdate()
    data class Error(val message: String) : ArenaRoomUpdate()
}
