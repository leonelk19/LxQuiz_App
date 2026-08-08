package com.example.lxquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.core.designsystem.theme.GreenPrimary
import com.example.lxquiz.core.designsystem.theme.LxquizTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.lxquiz.ui.viewmodels.QuizViewModel

@Composable
fun LoadingScreen(
    subject: String,
    viewModel: QuizViewModel,
    onFinished: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startQuiz(subject)
    }

    LaunchedEffect(uiState.questions) {
        if (uiState.questions.isNotEmpty()) {
            onFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = GreenPrimary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "LxQuiz Intelligence",
                color = GreenPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                if (uiState.error != null) "Error: ${uiState.error}" 
                else "Crafting your $subject evaluation...",
                color = if (uiState.error != null) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            
            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.startQuiz(subject) }) {
                    Text("Retry")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    LxquizTheme {
        // Mocked or simplified view for preview if needed, 
        // but here we just show the stateful one with a dummy if possible
        // Actually, better to have a LoadingScreenContent for preview
    }
}
