package com.example.lxquiz.domain.usecase.competition

import com.example.lxquiz.domain.repository.ArenaRoomUpdate
import com.example.lxquiz.domain.repository.IArenaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveArenaUpdatesUseCase @Inject constructor(
    private val repository: IArenaRepository
) {
    operator fun invoke(roomId: String): Flow<ArenaRoomUpdate> {
        return repository.observeRoomUpdates(roomId)
    }
}
