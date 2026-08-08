package com.example.lxquiz.data.repository

import com.example.lxquiz.data.models.UserProfile
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.repository.IAuthRepository
import com.example.lxquiz.domain.repository.IUserRepository
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userRepository: IUserRepository
) : IAuthRepository {
    
    companion object {
        private val localUsers = mutableMapOf<String, String>() // Email to Password
        private var loggedInUser: UserProfile? = null
        
        init {
            localUsers["test@example.com"] = "password123"
        }
    }

    override val currentUserProfile get() = loggedInUser

    override fun isUserLoggedIn(): Boolean = loggedInUser != null
    
    override fun isEmailRegistered(email: String): Boolean = localUsers.containsKey(email)

    override suspend fun signUp(email: String, pass: String, profile: UserProfile): NetworkResult<UserProfile> {
        delay(1000)
        
        if (localUsers.containsKey(email)) {
            return NetworkResult.Error(400, "User already exists with this email")
        }
        
        val uid = UUID.randomUUID().toString()
        val finalProfile = profile.copy(uid = uid, email = email)
        
        localUsers[email] = pass
        loggedInUser = finalProfile
        
        userRepository.saveLocalProfile(finalProfile)
        
        return NetworkResult.Success(finalProfile)
    }

    override suspend fun signIn(email: String, pass: String): NetworkResult<UserProfile> {
        delay(1000)
        
        val storedPass = localUsers[email]
        return if (storedPass != null && storedPass == pass) {
            val profile = userRepository.getLocalProfileByEmail(email)
            if (profile != null) {
                loggedInUser = profile
                NetworkResult.Success(profile)
            } else {
                NetworkResult.Error(404, "Profile not found")
            }
        } else {
            NetworkResult.Error(401, "Invalid email or password")
        }
    }

    override fun logout() {
        loggedInUser = null
    }
}
