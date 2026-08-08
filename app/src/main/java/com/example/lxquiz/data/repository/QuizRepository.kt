package com.example.lxquiz.data.repository

import com.example.lxquiz.data.models.QuizResult
import com.example.lxquiz.data.models.QuizSession
import com.example.lxquiz.data.remote.LxquizApiService
import com.example.lxquiz.data.remote.NetworkResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val apiService: LxquizApiService,
    private val auth: FirebaseAuth
) {
    private suspend fun getIdToken(): String {
        return auth.currentUser?.getIdToken(true)?.await()?.token?.let { "Bearer $it" } ?: ""
    }

    suspend fun generateQuiz(subjectId: String, count: Int): NetworkResult<QuizSession> {
        return try {
            val token = getIdToken()
            val response = apiService.generateQuiz(token, subjectId, count)
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    suspend fun submitResult(result: QuizResult): NetworkResult<QuizResult> {
        return try {
            val token = getIdToken()
            val response = apiService.submitQuizResult(token, result)
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
