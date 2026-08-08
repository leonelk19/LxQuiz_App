package com.example.lxquiz.data.repository

import com.example.lxquiz.data.models.ArenaConfig
import com.example.lxquiz.data.models.ArenaRoom
import com.example.lxquiz.data.remote.LxquizApiService
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.repository.ArenaRoomUpdate
import com.example.lxquiz.domain.repository.IArenaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class ArenaRepository @Inject constructor(
    private val apiService: LxquizApiService
) : IArenaRepository {

    override suspend fun createRoom(config: ArenaConfig): NetworkResult<ArenaRoom> {
        return try {
            val response = apiService.createArenaRoom("Bearer DEBUG", config)
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    override suspend fun joinRoom(roomId: String): NetworkResult<ArenaRoom> {
        return try {
            val response = apiService.joinArenaRoom("Bearer DEBUG", roomId)
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    override suspend fun leaveRoom(roomId: String): NetworkResult<Unit> {
        return try {
            val response = apiService.leaveArenaRoom("Bearer DEBUG", roomId)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    override suspend fun toggleReady(roomId: String, isReady: Boolean): NetworkResult<Unit> {
        return try {
            val response = apiService.toggleReady("Bearer DEBUG", roomId, isReady)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    override suspend fun startArena(roomId: String): NetworkResult<Unit> {
        return try {
            val response = apiService.startArena("Bearer DEBUG", roomId)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    override fun observeRoomUpdates(roomId: String): Flow<ArenaRoomUpdate> = flow {
        // In a real app, this would be a WebSocket connection.
        // For the groundwork, we simulate periodic polling or WS events.
        while (true) {
            val response = apiService.getArenaStatus("Bearer DEBUG", roomId)
            if (response.isSuccessful) {
                val room = response.body()!!
                emit(ArenaRoomUpdate.RoomState(room))
                if (room.status == com.example.lxquiz.data.models.ArenaStatus.STARTING) {
                    emit(ArenaRoomUpdate.MatchStarting)
                    break
                }
            }
            delay(3000.milliseconds)
        }
    }
}
