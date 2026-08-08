package com.example.lxquiz.domain.usecase.competition

import com.example.lxquiz.data.models.ArenaConfig
import com.example.lxquiz.data.models.ArenaRoom
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.repository.IArenaRepository
import javax.inject.Inject

class CreateArenaRoomUseCase @Inject constructor(
    private val repository: IArenaRepository
) {
    suspend operator fun invoke(config: ArenaConfig): NetworkResult<ArenaRoom> {
        return repository.createRoom(config)
    }
}
