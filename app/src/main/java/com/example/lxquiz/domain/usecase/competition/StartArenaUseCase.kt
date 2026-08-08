package com.example.lxquiz.domain.usecase.competition

import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.repository.IArenaRepository
import javax.inject.Inject

class StartArenaUseCase @Inject constructor(
    private val repository: IArenaRepository
) {
    suspend operator fun invoke(roomId: String): NetworkResult<Unit> {
        return repository.startArena(roomId)
    }
}
