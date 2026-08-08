package com.example.lxquiz.domain.usecase.auth

import com.example.lxquiz.domain.repository.IAuthRepository
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    operator fun invoke(): Boolean {
        return repository.isUserLoggedIn()
    }
}
