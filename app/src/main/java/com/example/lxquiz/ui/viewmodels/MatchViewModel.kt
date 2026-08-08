package com.example.lxquiz.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.repository.CompetitionResult
import com.example.lxquiz.domain.repository.ICompetitionRepository
import com.example.lxquiz.domain.repository.MatchAction
import com.example.lxquiz.domain.repository.MatchUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchUiState(
    val playerScores: Map<String, Int> = emptyMap(),
    val matchStatus: String = "Waiting",
    val isFinished: Boolean = false,
    val winnerId: String? = null,
    val syncPending: Boolean = false
)

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val repository: ICompetitionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchUiState())
    val uiState: StateFlow<MatchUiState> = _uiState.asStateFlow()

    fun joinMatch(matchId: String) {
        viewModelScope.launch {
            repository.observeMatchUpdates(matchId).collect { update ->
                when (update) {
                    is MatchUpdate.PlayerScore -> {
                        _uiState.update { it.copy(
                            playerScores = it.playerScores + (update.userId to update.score)
                        )}
                    }
                    is MatchUpdate.MatchEnded -> {
                        _uiState.update { it.copy(
                            isFinished = true,
                            winnerId = update.winnerId,
                            playerScores = update.finalScores
                        )}
                        // Save locally for offline record/sync
                        repository.saveResultLocally(
                            CompetitionResult(
                                tournamentId = null,
                                matchId = matchId,
                                score = update.finalScores.values.maxOrNull() ?: 0,
                                correctAnswers = 0, // Should be calculated
                                totalQuestions = 0,
                                timeTakenSeconds = 0
                            )
                        )
                    }
                    is MatchUpdate.Error -> {
                        _uiState.update { it.copy(matchStatus = "Error: ${update.message}") }
                    }
                }
            }
        }
    }

    fun submitAnswer(matchId: String, questionId: String, optionIndex: Int) {
        viewModelScope.launch {
            repository.sendMatchAction(matchId, MatchAction.SubmitAnswer(questionId, optionIndex))
        }
    }

    fun syncResults() {
        viewModelScope.launch {
            _uiState.update { it.copy(syncPending = true) }
            repository.syncOfflineResults()
            _uiState.update { it.copy(syncPending = false) }
        }
    }
}
