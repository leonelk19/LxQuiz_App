package com.example.lxquiz.domain.repository

import com.example.lxquiz.data.models.UserProfile
import com.example.lxquiz.data.remote.NetworkResult

interface IAuthRepository {
    val currentUserProfile: UserProfile?
    fun isUserLoggedIn(): Boolean
    fun isEmailRegistered(email: String): Boolean
    suspend fun signUp(email: String, pass: String, profile: UserProfile): NetworkResult<UserProfile>
    suspend fun signIn(email: String, pass: String): NetworkResult<UserProfile>
    fun logout()
}
