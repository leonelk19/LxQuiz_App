package com.example.lxquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.ui.components.StepItem
import com.example.lxquiz.ui.components.Divider
import com.example.lxquiz.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectSelectionScreen(
    field: String,
    onBack: () -> Unit,
    onContinueToQuiz: (String) -> Unit
) {
    var selectedSubject by remember { mutableStateOf<String?>(null) }
    
    val subjects = when(field) {
        "Technical" -> listOf("Mechanical Engineering", "Electrical Engineering", "Civil Engineering")
        "Sciences" -> listOf("Physics", "Chemistry", "Biology", "Mathematics")
        "Commercial" -> listOf("Accounting", "Business Math", "Commerce", "Marketing")
        "Arts" -> listOf("Literature", "History", "Philosophy", "Languages")
        else -> emptyList()
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("Take a Quiz", color = Color.White, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    Surface(
                        color = GreenPrimary,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text("JS", color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                StepItem(1, "Choose Field", false, isCompleted = true)
                Spacer(modifier = Modifier.width(8.dp))
                Divider(modifier = Modifier.width(24.dp).height(1.dp), color = GreenPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                StepItem(2, "Select Subject", true)
                Spacer(modifier = Modifier.width(8.dp))
                Divider(modifier = Modifier.width(24.dp).height(1.dp), color = Color.DarkGray)
                Spacer(modifier = Modifier.width(8.dp))
                StepItem(3, "Start Quiz", false)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Field: $field", color = Color.Gray, fontSize = 10.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Select a Subject", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Choose a subject within your field to start a focused quiz", color = Color.Gray, fontSize = 14.sp, textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier.height(32.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.heightIn(max = 300.dp)
                    ) {
                        items(subjects) { subject ->
                            SubjectChip(
                                title = subject,
                                isSelected = selectedSubject == subject,
                                onClick = { selectedSubject = subject }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        TextButton(onClick = onBack) {
                            Text("← Back to Fields", color = Color.Gray)
                        }
                        Button(
                            onClick = { selectedSubject?.let { onContinueToQuiz(it) } },
                            enabled = selectedSubject != null,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GreenPrimary,
                                disabledContainerColor = Color.DarkGray
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Continue to Quiz →", color = if(selectedSubject != null) Color.Black else Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectChip(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (isSelected) GreenPrimary.copy(alpha = 0.1f) else Color.DarkGray.copy(alpha = 0.3f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, if (isSelected) GreenPrimary else Color.Transparent, RoundedCornerShape(12.dp))
    ) {
        Box(modifier = Modifier.padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
            Text(title, color = if (isSelected) GreenPrimary else Color.White, fontSize = 12.sp, textAlign = TextAlign.Center)
        }
    }
}
