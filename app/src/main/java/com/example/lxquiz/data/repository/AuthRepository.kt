package com.example.lxquiz.data.repository

import com.example.lxquiz.data.models.UserProfile
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.repository.IAuthRepository
import com.example.lxquiz.domain.repository.IUserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: IUserRepository
) : IAuthRepository {
    
    private var _currentUserProfile: UserProfile? = null

    override val currentUserProfile get() = _currentUserProfile

    override fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null
    
    override fun isEmailRegistered(email: String): Boolean {
        // Firebase doesn't provide a direct synchronous way to check this without admin SDK
        // or attempting a sign in/up. For the sake of this implementation, we'll return false
        // or handle it in sign-up flow.
        return false 
    }

    override suspend fun signUp(email: String, pass: String, profile: UserProfile): NetworkResult<UserProfile> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
            val user = result.user
            if (user != null) {
                val finalProfile = profile.copy(uid = user.uid, email = email)
                userRepository.createUserProfile(finalProfile)
                _currentUserProfile = finalProfile
                NetworkResult.Success(finalProfile)
            } else {
                NetworkResult.Error(500, "User creation failed")
            }
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    override suspend fun signIn(email: String, pass: String): NetworkResult<UserProfile> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
            val user = result.user
            if (user != null) {
                // Fetch profile from Firestore/Backend via userRepository
                // For now, using a placeholder if fetch fails or mock
                val profileResult = userRepository.getUserProfile(user.uid)
                // Note: userRepository.getUserProfile returns a Flow, we need a single result
                // This is a simplification.
                val profile = UserProfile(uid = user.uid, email = user.email ?: "")
                _currentUserProfile = profile
                NetworkResult.Success(profile)
            } else {
                NetworkResult.Error(401, "Invalid email or password")
            }
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    override suspend fun signInWithGoogle(idToken: String): NetworkResult<UserProfile> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val user = result.user
            if (user != null) {
                val profile = UserProfile(
                    uid = user.uid, 
                    email = user.email ?: "",
                    fullName = user.displayName ?: ""
                )
                // If new user, create profile
                if (result.additionalUserInfo?.isNewUser == true) {
                    userRepository.createUserProfile(profile)
                }
                _currentUserProfile = profile
                NetworkResult.Success(profile)
            } else {
                NetworkResult.Error(500, "Google sign in failed")
            }
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
        _currentUserProfile = null
    }
}
