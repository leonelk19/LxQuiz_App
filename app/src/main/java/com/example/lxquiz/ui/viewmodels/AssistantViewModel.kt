package com.example.lxquiz.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lxquiz.data.models.AiChatMessage
import com.example.lxquiz.data.models.MessageStatus
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.data.repository.AssistantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class AssistantUiState(
    val messages: List<AiChatMessage> = listOf(
        AiChatMessage(text = "Hello! I'm Lx AI. How can I assist your learning today?", isUser = false)
    ),
    val inputText: String = "",
    val isAiThinking: Boolean = false
)

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val repository: AssistantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AssistantUiState())
    val uiState: StateFlow<AssistantUiState> = _uiState.asStateFlow()

    fun onInputChanged(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun sendMessage() {
        val text = _uiState.value.inputText
        if (text.isBlank()) return

        val userMessage = AiChatMessage(
            id = "MSG-" + System.currentTimeMillis(),
            text = text,
            isUser = true
        )

        _uiState.update { 
            it.copy(
                messages = it.messages + userMessage,
                inputText = "",
                isAiThinking = true
            )
        }

        viewModelScope.launch {
            when (val result = repository.sendMessage(userMessage)) {
                is NetworkResult.Success -> {
                    _uiState.update { 
                        it.copy(
                            messages = it.messages + result.data,
                            isAiThinking = false
                        )
                    }
                }
                else -> {
                    val errorMessage = AiChatMessage(
                        text = "Sorry, I encountered an error. Please try again.",
                        isUser = false,
                        status = MessageStatus.ERROR
                    )
                    _uiState.update { 
                        it.copy(
                            messages = it.messages + errorMessage,
                            isAiThinking = false
                        )
                    }
                }
            }
        }
    }
}
