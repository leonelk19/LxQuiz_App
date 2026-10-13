package com.example.lxquiz.ui.screens.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.core.designsystem.LxDivider
import com.example.lxquiz.core.designsystem.StepItem
import com.example.lxquiz.core.designsystem.theme.GreenDark
import com.example.lxquiz.core.designsystem.theme.GreenPrimary
import com.example.lxquiz.data.models.SyllabusTopic
import com.example.lxquiz.data.repository.CurriculumRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizTopicScreen(
    subject: String,
    onBack: () -> Unit,
    onStartQuiz: (String) -> Unit
) {
    val repository = remember { CurriculumRepository() }
    val topics = remember(subject) { repository.getSyllabusTopics(subject) }

    var selectedTopic by remember { mutableStateOf<SyllabusTopic?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(subject, color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    val targetTopic = if (selectedTopic != null) {
                        "$subject: ${selectedTopic!!.name}"
                    } else {
                        "$subject: Comprehensive"
                    }
                    onStartQuiz(targetTopic)
                },
                containerColor = GreenPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null) },
                text = { Text("Start Quiz", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Steps indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                ) {
                    StepItem(1, "Subject", isActive = false, isCompleted = true)
                    LxDivider(modifier = Modifier.weight(1f).padding(horizontal = 6.dp).height(1.dp), color = GreenPrimary)
                    StepItem(2, "Topic", isActive = true, isCompleted = selectedTopic != null)
                    LxDivider(modifier = Modifier.weight(1f).padding(horizontal = 6.dp).height(1.dp), color = if (selectedTopic != null) GreenPrimary else MaterialTheme.colorScheme.outlineVariant)
                    StepItem(3, "Quiz", isActive = false, isCompleted = false)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Topic Selection Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Topic, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Select Syllabus Topic", color = GreenDark, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Choose a specific topic from all syllabus topics in $subject", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Overall subject option
                        Surface(
                            onClick = { selectedTopic = null },
                            color = if (selectedTopic == null) GreenPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, if (selectedTopic == null) GreenPrimary else Color.Transparent, RoundedCornerShape(12.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("All Topics (Full Subject Test)", color = GreenDark, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text("Generates a comprehensive quiz covering all topics", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                                }
                                if (selectedTopic == null) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GreenPrimary)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Syllabus Topics", color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(topics) { topic ->
                val isSelected = selectedTopic?.id == topic.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { selectedTopic = topic },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) GreenPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, if (isSelected) GreenPrimary else MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(topic.name, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Badge(
                                    containerColor = when (topic.difficulty.lowercase()) {
                                        "easy" -> Color(0xFF4CAF50)
                                        "hard" -> Color(0xFFF44336)
                                        else -> Color(0xFFFF9800)
                                    },
                                    contentColor = Color.White
                                ) {
                                    Text(topic.difficulty, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 4.dp))
                                }
                            }
                            if (topic.description.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(topic.description, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                            }
                        }
                        if (isSelected) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
