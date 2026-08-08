package com.example.lxquiz.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lxquiz.data.models.Question
import com.example.lxquiz.data.models.QuizResult
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.data.repository.QuizRepository
import com.example.lxquiz.domain.manager.IProctoringManager
import com.example.lxquiz.domain.manager.ISecurityManager
import com.example.lxquiz.domain.manager.ProctoringEvent
import com.example.lxquiz.domain.usecase.security.ReportIntegrityEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizUiState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedOptionIndex: Int = -1,
    val userAnswers: Map<Int, Int> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val quizResult: QuizResult? = null,
    val isQuizFinished: Boolean = false,
    val timeLeftSeconds: Int = 0,
    val integrityWarning: String? = null
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository,
    private val securityManager: ISecurityManager,
    private val proctoringManager: IProctoringManager,
    private val reportIntegrityEventUseCase: ReportIntegrityEventUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var currentSessionId: String = "SESSION-" + System.currentTimeMillis()

    init {
        monitorIntegrity()
    }

    private fun monitorIntegrity() {
        viewModelScope.launch {
            proctoringManager.proctoringEvents.collect { event ->
                handleIntegrityEvent(event)
            }
        }
        viewModelScope.launch {
            securityManager.observeFocusLoss().collect {
                handleIntegrityEvent(ProctoringEvent.UserLookingAway)
            }
        }
        // Periodic check for external devices
        viewModelScope.launch {
            while (true) {
                if (securityManager.detectExternalDisplays()) {
                    handleIntegrityEvent(ProctoringEvent.ExternalDisplayDetected)
                }
                if (securityManager.detectExternalInputDevices()) {
                    handleIntegrityEvent(ProctoringEvent.ExternalInputDetected)
                }
                kotlinx.coroutines.delay(5000)
            }
        }
    }

    private fun handleIntegrityEvent(event: ProctoringEvent) {
        viewModelScope.launch {
            reportIntegrityEventUseCase(event, currentSessionId)
            // HARD-CODED PUNISHMENT: Immediate failure on critical violations
            if (event is ProctoringEvent.UserLookingAway || 
                event is ProctoringEvent.AbnormalSensorReading ||
                event is ProctoringEvent.ExternalDisplayDetected ||
                event is ProctoringEvent.ExternalInputDetected
            ) {
                _uiState.update { 
                    it.copy(
                        error = "QUIZ TERMINATED: Security violation detected (${event.javaClass.simpleName}). Your attempt has been voided.",
                        isQuizFinished = true,
                        timeLeftSeconds = 0
                    )
                }
                proctoringManager.stopProctoring()
            } else {
                _uiState.update { 
                    it.copy(integrityWarning = "Anti-Cheat Alert: ${event.javaClass.simpleName}. Continued violations will terminate the quiz.")
                }
            }
        }
    }

    fun startQuiz(subjectId: String, count: Int = 10) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            // Security measures
            securityManager.clearClipboard()
            proctoringManager.startSensorProctoring()
            proctoringManager.startCameraProctoring()
            
            when (val result = repository.generateQuiz(subjectId, count)) {
                is NetworkResult.Success -> {
                    _uiState.update { 
                        it.copy(
                            questions = result.data.questions,
                            timeLeftSeconds = result.data.timeLimitSeconds,
                            isLoading = false
                        )
                    }
                }
                is NetworkResult.Error -> _uiState.update { it.copy(error = result.message, isLoading = false) }
                is NetworkResult.Exception -> _uiState.update { it.copy(error = result.e.message, isLoading = false) }
                NetworkResult.Loading -> _uiState.update { it.copy(isLoading = true) }
            }
        }
    }

    fun selectOption(index: Int) {
        _uiState.update { it.copy(selectedOptionIndex = index) }
    }

    fun nextQuestion() {
        val currentState = _uiState.value
        val updatedAnswers = currentState.userAnswers.toMutableMap()
        updatedAnswers[currentState.currentQuestionIndex] = currentState.selectedOptionIndex

        if (currentState.currentQuestionIndex < currentState.questions.size - 1) {
            _uiState.update { 
                it.copy(
                    currentQuestionIndex = it.currentQuestionIndex + 1,
                    selectedOptionIndex = it.userAnswers[it.currentQuestionIndex + 1] ?: -1,
                    userAnswers = updatedAnswers
                )
            }
        } else {
            submitQuiz(updatedAnswers)
        }
    }

    fun previousQuestion() {
        if (_uiState.value.currentQuestionIndex > 0) {
            _uiState.update { 
                it.copy(
                    currentQuestionIndex = it.currentQuestionIndex - 1,
                    selectedOptionIndex = it.userAnswers[it.currentQuestionIndex - 1] ?: -1
                )
            }
        }
    }

    fun dismissIntegrityWarning() {
        _uiState.update { it.copy(integrityWarning = null) }
    }

    private fun submitQuiz(answers: Map<Int, Int>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            proctoringManager.stopProctoring()
            
            val questions = _uiState.value.questions
            var correctCount = 0
            questions.forEachIndexed { index, question ->
                if (answers[index] == question.correctAnswerIndex) {
                    correctCount++
                }
            }

            val result = QuizResult(
                sessionId = "SESSION-" + System.currentTimeMillis(),
                score = (correctCount.toFloat() / questions.size * 100).toInt(),
                totalQuestions = questions.size,
                correctAnswers = correctCount,
                timeTakenSeconds = 300, // Placeholder
                xpEarned = correctCount * 10
            )

            when (val submission = repository.submitResult(result)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(quizResult = submission.data, isQuizFinished = true, isLoading = false) }
                }
                else -> {
                    _uiState.update { it.copy(quizResult = result, isQuizFinished = true, isLoading = false) }
                }
            }
        }
    }
}
