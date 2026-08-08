package com.example.lxquiz.data.models

import kotlinx.serialization.Serializable

@Serializable
data class IntegrityEventRequest(
    val event_type: String,
    val session_id: String,
    val details: String? = null,
    val timestamp: String = ""
)

@Serializable
data class IntegrityResponse(
    val status: String,
    val action: String? = null,
    val message: String? = null
)
