package com.example.lxquiz.domain.usecase.security

import com.apollographql.apollo.ApolloClient
import com.example.lxquiz.domain.manager.ProctoringEvent
import com.example.lxquiz.data.remote.NetworkResult
import javax.inject.Inject

class ReportIntegrityEventUseCase @Inject constructor(
    private val apolloClient: ApolloClient
) {
    suspend operator fun invoke(event: ProctoringEvent, sessionId: String = "CURRENT_SESSION"): NetworkResult<Unit> {
        return try {
            // In a real app, you'd use the generated Mutation class:
            // val input = IntegrityEventInput(eventType = event.javaClass.simpleName, sessionId = sessionId)
            // val response = apolloClient.mutation(ReportIntegrityMutation(input)).execute()
            
            println("LAC ALERT (GQL): $event")
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }
}
