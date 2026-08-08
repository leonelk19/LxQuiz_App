package com.example.lxquiz.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lxquiz.data.models.UserProfile
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.usecase.auth.GetCurrentUserProfileUseCase
import com.example.lxquiz.domain.usecase.auth.LogoutUseCase
import com.example.lxquiz.domain.usecase.user.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedOut: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentProfile = getCurrentUserProfileUseCase()
            if (currentProfile != null) {
                val result = getUserProfileUseCase(currentProfile.uid).first()
                if (result is NetworkResult.Success) {
                    _uiState.update { it.copy(profile = result.data, isLoading = false) }
                } else if (result is NetworkResult.Error) {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun logout() {
        logoutUseCase()
        _uiState.update { it.copy(isLoggedOut = true) }
    }
}
