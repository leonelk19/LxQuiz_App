package com.example.lxquiz.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lxquiz.data.models.LeaderboardEntry
import com.example.lxquiz.data.models.Tournament
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.repository.ICompetitionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CompetitionUiState(
    val tournaments: List<Tournament> = emptyList(),
    val leaderboard: List<LeaderboardEntry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedTab: Int = 0 // 0: Global, 1: Continental, 2: National
)

@HiltViewModel
class CompetitionViewModel @Inject constructor(
    private val repository: ICompetitionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompetitionUiState())
    val uiState: StateFlow<CompetitionUiState> = _uiState.asStateFlow()

    init {
        loadTournaments()
        loadLeaderboard("global")
    }

    fun loadTournaments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = repository.getTournaments()) {
                is NetworkResult.Success -> _uiState.update { it.copy(tournaments = result.data, isLoading = false) }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun loadLeaderboard(type: String, region: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = repository.getLeaderboard(type, region)) {
                is NetworkResult.Success -> _uiState.update { it.copy(leaderboard = result.data, isLoading = false) }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun selectTab(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
        val type = when(index) {
            0 -> "global"
            1 -> "continental"
            else -> "national"
        }
        loadLeaderboard(type)
    }
}
