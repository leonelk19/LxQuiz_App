package com.example.lxquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewAnswersScreen(
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("Review Answers", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { padding ->
        val questions = listOf(
            QuestionReview("What is the capital of France?", "Paris", "Paris", true),
            QuestionReview("Which planet is known as the Red Planet?", "Mars", "Jupiter", false),
            QuestionReview("What is the powerhouse of the cell?", "Mitochondria", "Mitochondria", true)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            itemsIndexed(questions) { index, question ->
                ReviewItem(index + 1, question)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class QuestionReview(val question: String, val correctAnswer: String, val userAnswer: String, val isCorrect: Boolean)

@Composable
fun ReviewItem(number: Int, review: QuestionReview) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Q$number. ", color = GreenPrimary, fontWeight = FontWeight.Bold)
                Text(review.question, color = Color.White, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            AnswerRow("Your Answer:", review.userAnswer, if (review.isCorrect) Color(0xFF00C853) else Color(0xFFFF3D00), if (review.isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel)
            
            if (!review.isCorrect) {
                Spacer(modifier = Modifier.height(8.dp))
                AnswerRow("Correct Answer:", review.correctAnswer, Color(0xFF00C853), Icons.Default.CheckCircle)
            }
        }
    }
}

@Composable
fun AnswerRow(label: String, answer: String, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(100.dp))
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(answer, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
