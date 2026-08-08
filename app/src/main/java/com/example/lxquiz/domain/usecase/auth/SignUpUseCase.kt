package com.example.lxquiz.domain.usecase.auth

import com.example.lxquiz.data.models.UserProfile
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.repository.IAuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(email: String, pass: String, profile: UserProfile): NetworkResult<UserProfile> {
        return repository.signUp(email, pass, profile)
    }
}
