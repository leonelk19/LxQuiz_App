package com.example.lxquiz.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lxquiz.data.models.Country
import com.example.lxquiz.data.models.EducationLevel
import com.example.lxquiz.data.models.UserProfile
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.usecase.auth.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val profile: UserProfile) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val isEmailRegisteredUseCase: IsEmailRegisteredUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    var tempFullName: String = ""
    var tempEmail: String = ""
    var tempCountry: Country? = null
    var tempLevel: EducationLevel? = null
    var tempPassword: String = ""

    fun signUp(subLevelId: String, sectionId: String?, selectedSubjects: List<String>) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            val profile = UserProfile(
                fullName = tempFullName,
                email = tempEmail,
                countryId = tempCountry?.id ?: "",
                educationLevel = tempLevel,
                subLevelId = subLevelId,
                sectionId = sectionId ?: "",
                selectedSubjects = selectedSubjects
            )

            val result = signUpUseCase(tempEmail, tempPassword, profile)
            
            when (result) {
                is NetworkResult.Success -> _authState.value = AuthState.Success(result.data)
                is NetworkResult.Error -> _authState.value = AuthState.Error(result.message)
                is NetworkResult.Exception -> _authState.value = AuthState.Error("Error: ${result.e.message}")
                NetworkResult.Loading -> _authState.value = AuthState.Loading
            }
        }
    }

    fun signIn(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = signInUseCase(email, pass)
            when (result) {
                is NetworkResult.Success -> _authState.value = AuthState.Success(result.data)
                is NetworkResult.Error -> _authState.value = AuthState.Error(result.message)
                is NetworkResult.Exception -> _authState.value = AuthState.Error(result.e.message ?: "Unknown error")
                else -> {}
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = signInWithGoogleUseCase(idToken)
            when (result) {
                is NetworkResult.Success -> _authState.value = AuthState.Success(result.data)
                is NetworkResult.Error -> _authState.value = AuthState.Error(result.message)
                is NetworkResult.Exception -> _authState.value = AuthState.Error(result.e.message ?: "Unknown error")
                else -> {}
            }
        }
    }

    fun isEmailRegistered(email: String): Boolean {
        return isEmailRegisteredUseCase(email)
    }

    fun setError(message: String) {
        _authState.value = AuthState.Error(message)
    }
    
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
