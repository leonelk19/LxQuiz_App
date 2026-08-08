package com.example.lxquiz.domain.usecase.auth

import com.example.lxquiz.domain.repository.IAuthRepository
import javax.inject.Inject

class IsEmailRegisteredUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    operator fun invoke(email: String): Boolean {
        return repository.isEmailRegistered(email)
    }
}
