package com.example.lxquiz.domain.usecase.auth

import com.example.lxquiz.data.models.UserProfile
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.repository.IAuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(idToken: String): NetworkResult<UserProfile> {
        return repository.signInWithGoogle(idToken)
    }
}
