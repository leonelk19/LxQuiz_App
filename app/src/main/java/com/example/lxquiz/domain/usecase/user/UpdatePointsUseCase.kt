package com.example.lxquiz.domain.usecase.user

import com.example.lxquiz.domain.repository.IUserRepository
import javax.inject.Inject

class UpdatePointsUseCase @Inject constructor(
    private val repository: IUserRepository
) {
    suspend operator fun invoke(uid: String, pointsToAdd: Int) {
        repository.updatePoints(uid, pointsToAdd)
    }
}
