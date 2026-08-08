package com.example.lxquiz.domain.usecase.auth

import com.example.lxquiz.data.models.UserProfile
import com.example.lxquiz.domain.repository.IAuthRepository
import javax.inject.Inject

class GetCurrentUserProfileUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    operator fun invoke(): UserProfile? {
        return repository.currentUserProfile
    }
}
