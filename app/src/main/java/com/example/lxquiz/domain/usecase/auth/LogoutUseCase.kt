package com.example.lxquiz.domain.usecase.auth

import com.example.lxquiz.domain.repository.IAuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    operator fun invoke() {
        repository.logout()
    }
}
