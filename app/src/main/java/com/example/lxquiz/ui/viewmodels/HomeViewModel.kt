package com.example.lxquiz.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lxquiz.data.models.Tournament
import com.example.lxquiz.data.models.UserProfile
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.usecase.auth.GetCurrentUserProfileUseCase
import com.example.lxquiz.domain.usecase.competition.GetTournamentsUseCase
import com.example.lxquiz.domain.usecase.user.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val profile: UserProfile? = null,
    val featuredTournaments: List<Tournament> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getTournamentsUseCase: GetTournamentsUseCase,
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val currentProfile = getCurrentUserProfileUseCase()
            if (currentProfile != null) {
                val profileResult = getUserProfileUseCase(currentProfile.uid).first()
                if (profileResult is NetworkResult.Success) {
                    _uiState.update { it.copy(profile = profileResult.data) }
                }
            }

            when (val tournamentResult = getTournamentsUseCase()) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(featuredTournaments = tournamentResult.data, isLoading = false) }
                }
                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}
