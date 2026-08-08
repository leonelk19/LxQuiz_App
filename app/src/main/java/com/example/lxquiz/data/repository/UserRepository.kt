package com.example.lxquiz.data.repository

import com.apollographql.apollo.ApolloClient
import com.example.lxquiz.data.models.UserProfile
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.repository.IUserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apolloClient: ApolloClient
) : IUserRepository {
    companion object {
        private val profiles = mutableMapOf<String, UserProfile>()
        
        init {
            val testUid = "test-uid-123"
            profiles[testUid] = UserProfile(
                uid = testUid,
                fullName = "Test User",
                email = "test@example.com",
                points = 1250,
                streak = 5
            )
        }
    }

    override suspend fun saveLocalProfile(profile: UserProfile) {
        profiles[profile.uid] = profile
    }

    override fun getLocalProfileByEmail(email: String): UserProfile? {
        return profiles.values.find { it.email == email }
    }

    override suspend fun createUserProfile(profile: UserProfile): NetworkResult<UserProfile> {
        delay(500)
        profiles[profile.uid] = profile
        return NetworkResult.Success(profile)
    }

    override fun getUserProfile(uid: String): Flow<NetworkResult<UserProfile>> = flow {
        emit(NetworkResult.Loading)
        
        try {
            // Using GraphQL for remote profile fetching
            // val response = apolloClient.query(GetUserProfileQuery(uid)).execute()
            // if (response.data != null) { ... }
            
            // Fallback to local map for now until build generates GraphQL classes
            delay(500)
            val profile = profiles[uid]
            if (profile != null) {
                emit(NetworkResult.Success(profile))
            } else {
                emit(NetworkResult.Error(404, "Profile not found"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Exception(e))
        }
    }
    
    override suspend fun updatePoints(uid: String, pointsToAdd: Int) {
        val current = profiles[uid]
        if (current != null) {
            profiles[uid] = current.copy(points = current.points + pointsToAdd)
        }
    }
}
