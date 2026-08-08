package com.example.lxquiz.data.repository

import com.example.lxquiz.data.models.AiChatMessage
import com.example.lxquiz.data.remote.LxquizApiService
import com.example.lxquiz.data.remote.NetworkResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssistantRepository @Inject constructor(
    private val apiService: LxquizApiService,
    private val auth: FirebaseAuth
) {
    private suspend fun getIdToken(): String {
        return auth.currentUser?.getIdToken(true)?.await()?.token?.let { "Bearer $it" } ?: ""
    }

    suspend fun sendMessage(message: AiChatMessage): NetworkResult<AiChatMessage> {
        return try {
            val token = getIdToken()
            val response = apiService.sendMessageToAi(token, message)
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }
}
