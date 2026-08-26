package com.example.lxquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import com.example.lxquiz.core.designsystem.BottomNavigationBar
import com.example.lxquiz.core.designsystem.theme.GreenPrimary

import androidx.compose.ui.draw.clip
import androidx.compose.material3.MaterialTheme
import com.example.lxquiz.core.designsystem.theme.LxquizTheme

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import com.example.lxquiz.core.security.SecurityManager
import com.example.lxquiz.ui.viewmodels.QuizUiState
import com.example.lxquiz.ui.viewmodels.QuizViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    onBack: () -> Unit,
    onComplete: () -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity
    
    // We would ideally inject this, but for the lockdown/secure flag we need the activity context
    val securityManager = remember { SecurityManager(context.applicationContext) }

    LaunchedEffect(Unit) {
        activity?.let {
            securityManager.setSecureScreen(it, true)
            securityManager.startLockdown(it)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            activity?.let {
                securityManager.setSecureScreen(it, false)
                securityManager.stopLockdown(it)
            }
        }
    }

    LaunchedEffect(uiState.isQuizFinished) {
        if (uiState.isQuizFinished) {
            onComplete()
        }
    }

    if (uiState.integrityWarning != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissIntegrityWarning() },
            title = { Text("Integrity Warning") },
            text = { Text(uiState.integrityWarning!!) },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissIntegrityWarning() }) {
                    Text("I Understand")
                }
            }
        )
    }

    QuizScreenContent(
        uiState = uiState,
        onBack = onBack,
        onNavigate = onNavigate,
        onOptionSelected = { viewModel.selectOption(it) },
        onNext = { viewModel.nextQuestion() },
        onPrevious = { viewModel.previousQuestion() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreenContent(
    uiState: QuizUiState,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    onOptionSelected: (Int) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    val currentQuestion = uiState.questions.getOrNull(uiState.currentQuestionIndex)
    val totalQuestions = uiState.questions.size

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Question ${uiState.currentQuestionIndex + 1} of $totalQuestions", 
                            color = GreenPrimary, 
                            fontSize = 16.sp, 
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(GreenPrimary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            val minutes = uiState.timeLeftSeconds / 60
                            val seconds = uiState.timeLeftSeconds % 60
                            Text(
                                String.format(Locale.US, "%d:%02d", minutes, seconds), 
                                color = GreenPrimary, 
                                fontSize = 14.sp, 
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(48.dp)) // Offset for back button
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = GreenPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onPrevious,
                        enabled = uiState.currentQuestionIndex > 0,
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary)
                    ) {
                        Text("Previous")
                    }
                    Button(
                        onClick = onNext,
                        enabled = uiState.selectedOptionIndex != -1 || uiState.isLoading,
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                        } else {
                            Text(
                                if (uiState.currentQuestionIndex == totalQuestions - 1) "Finish" else "Next", 
                                color = Color.White, 
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                BottomNavigationBar(currentScreen = "quiz", onNavigate = onNavigate)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            val progress = if (totalQuestions > 0) (uiState.currentQuestionIndex + 1).toFloat() / totalQuestions else 0f
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                color = GreenPrimary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            
            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(uiState.error, color = Color.Red, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }

            currentQuestion?.let { question ->
                Spacer(modifier = Modifier.height(24.dp))
                Text(question.difficulty.uppercase(), color = GreenPrimary, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    question.text,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(32.dp))

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    question.options.forEachIndexed { index, option ->
                        OptionCard(
                            text = option,
                            isSelected = uiState.selectedOptionIndex == index,
                            onClick = { onOptionSelected(index) }
                        )
                    }
                }
            } ?: run {
                if (!uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No questions available.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun OptionCard(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) GreenPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) GreenPrimary else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(2.dp, if (isSelected) GreenPrimary else MaterialTheme.colorScheme.outline, CircleShape)
                    .padding(4.dp)
            ) {
                if (isSelected) {
                    Box(modifier = Modifier.fillMaxSize().background(GreenPrimary, CircleShape))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text, color = if (isSelected) GreenPrimary else MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    LxquizTheme {
        QuizScreenContent(
            uiState = QuizUiState(),
            onBack = {},
            onNavigate = {},
            onOptionSelected = {},
            onNext = {},
            onPrevious = {}
        )
    }
}
