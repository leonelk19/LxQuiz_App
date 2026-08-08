package com.example.lxquiz.domain.repository

import com.example.lxquiz.data.models.UserProfile
import com.example.lxquiz.data.remote.NetworkResult
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun saveLocalProfile(profile: UserProfile)
    fun getLocalProfileByEmail(email: String): UserProfile?
    suspend fun createUserProfile(profile: UserProfile): NetworkResult<UserProfile>
    fun getUserProfile(uid: String): Flow<NetworkResult<UserProfile>>
    suspend fun updatePoints(uid: String, pointsToAdd: Int)
}
