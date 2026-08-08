package com.example.lxquiz.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AiChatMessage(
    val id: String = "",
    val text: String = "",
    val isUser: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val status: MessageStatus = MessageStatus.SENT
)

enum class MessageStatus {
    SENDING, SENT, ERROR
}
