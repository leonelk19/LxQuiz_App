package com.example.lxquiz.domain.usecase.user

import com.example.lxquiz.data.models.UserProfile
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: IUserRepository
) {
    operator fun invoke(uid: String): Flow<NetworkResult<UserProfile>> {
        return repository.getUserProfile(uid)
    }
}
