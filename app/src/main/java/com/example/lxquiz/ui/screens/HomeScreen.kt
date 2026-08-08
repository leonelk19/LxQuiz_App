package com.example.lxquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.ui.components.BottomNavigationBar
import com.example.lxquiz.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onQuizSelected: (String) -> Unit,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("LxQuiz", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(currentScreen = "home", onNavigate = onNavigate)
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
                GreetingSection()
                Spacer(modifier = Modifier.height(24.dp))
                StatsSection()
                Spacer(modifier = Modifier.height(24.dp))
                SearchBar()
                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader("Featured Quizzes", "See All")
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(3) { index ->
                val (title, questions, difficulty, color) = when (index) {
                    0 -> Triple("Science Basics", "15 Questions • 80% Completed", "Intermediate")
                    1 -> Triple("Sports Trivia", "10 Questions • 0% Completed", "Hard")
                    else -> Triple("Art History", "12 Questions • 20% Completed", "Easy")
                }.let { (t, s, d) -> 
                    val c = when(d) {
                        "Intermediate" -> Color(0xFFC2FF3D)
                        "Hard" -> Color(0xFFFF3D00)
                        else -> Color(0xFF00C853)
                    }
                    QuizItemData(t, s, d, c)
                }
                QuizCategoryItem(title, questions, progress = index * 0.4f, difficulty, color, onClick = { onQuizSelected(title) })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class QuizItemData(val title: String, val subtitle: String, val difficulty: String, val difficultyColor: Color)

@Composable
fun GreetingSection() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(GreenPrimary, Color(0xFF006400))
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text("Hello, Alex!", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Ready for today's quiz? Challenge yourself now.", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun StatsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatItem("12", "Quizzes", Icons.AutoMirrored.Filled.LibraryBooks, Modifier.weight(1f))
        StatItem("84%", "Average", Icons.Default.Percent, Modifier.weight(1f))
        StatItem("7", "Streak", Icons.Default.Whatshot, Modifier.weight(1f))
    }
}

@Composable
fun StatItem(value: String, label: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(icon, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search quizzes, categories...", color = Color.Gray, fontSize = 14.sp) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1E1E1E),
            unfocusedContainerColor = Color(0xFF1E1E1E),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = GreenPrimary
        )
    )
}

@Composable
fun SectionHeader(title: String, action: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(action, color = GreenPrimary, fontSize = 14.sp)
    }
}

@Composable
fun QuizCategoryItem(title: String, subtitle: String, progress: Float, difficulty: String, difficultyColor: Color, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(difficultyColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Science, contentDescription = null, tint = difficultyColor)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(difficulty, color = difficultyColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Text(subtitle, color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                    color = difficultyColor,
                    trackColor = Color.DarkGray
                )
            }
        }
    }
}

