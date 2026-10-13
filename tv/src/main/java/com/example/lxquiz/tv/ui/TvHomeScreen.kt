package com.example.lxquiz.tv.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.core.designsystem.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvHomeScreen(
    onStartQuiz: (String) -> Unit,
    onOpenAiAssistant: () -> Unit
) {
    var generatedSubject by remember { mutableStateOf("Science & Technology") }
    var isListeningVoice by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFF0F172A) // Deep dark TV background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 48.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TV Header - Clean & Simple (No Login, No Navigation Icons)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "LxQuiz TV",
                    color = GreenPrimary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "AI Voice Assistant & Quiz Engine • No Login Required",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )
            }

            // Central AI Assistant & Alexa Voice Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(2.dp, GreenPrimary.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Alexa & AI Voice Quiz Generator", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Say or tap to ask Alexa / AI Assistant: \"Alexa, generate a Science quiz for TV\" or select subject below.",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                isListeningVoice = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isListeningVoice) Color.Red else Color(0xFF673AB7)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.height(56.dp).padding(horizontal = 12.dp)
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(if (isListeningVoice) "Listening..." else "Speak Voice Command", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onStartQuiz(generatedSubject) },
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.height(56.dp).padding(horizontal = 12.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Start Quiz Now", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }

            // Quick Subject Selection Cards for TV
            Row(
                modifier = Modifier.fillMaxWidth(0.85f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                listOf("Science", "Mathematics", "History", "General Knowledge").forEach { subject ->
                    Card(
                        onClick = {
                            generatedSubject = subject
                            onStartQuiz(subject)
                        },
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = if (generatedSubject == subject) GreenPrimary.copy(alpha = 0.2f) else Color(0xFF1E293B)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            width = if (generatedSubject == subject) 2.dp else 1.dp,
                            color = if (generatedSubject == subject) GreenPrimary else Color.White.copy(alpha = 0.1f)
                        )
                    ) {
                        Box(modifier = Modifier.padding(20.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(subject, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
