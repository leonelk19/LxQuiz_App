package com.example.lxquiz.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.core.designsystem.AssistantFAB
import com.example.lxquiz.core.designsystem.BottomNavigationBar
import com.example.lxquiz.core.designsystem.theme.GreenPrimary
import com.example.lxquiz.core.designsystem.theme.GreenDark
import com.example.lxquiz.core.designsystem.theme.GreenSecondary
import com.example.lxquiz.core.designsystem.theme.LxquizTheme

import androidx.compose.runtime.collectAsState
import com.example.lxquiz.ui.viewmodels.CompetitionUiState
import com.example.lxquiz.ui.viewmodels.CompetitionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lxquiz.data.models.TournamentStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompetitionScreen(
    onNavigate: (String) -> Unit,
    viewModel: CompetitionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CompetitionScreenContent(
        uiState = uiState,
        onNavigate = onNavigate,
        onRefresh = { viewModel.loadTournaments() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompetitionScreenContent(
    uiState: CompetitionUiState,
    onNavigate: (String) -> Unit,
    onRefresh: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "COMPETITIONS",
                            color = GreenPrimary,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = Color(0xFFFFD700),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                "PRO",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                color = Color.Black,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = GreenPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            BottomNavigationBar(currentScreen = "competition", onNavigate = onNavigate)
        },
        floatingActionButton = {
            AssistantFAB(onClick = { onNavigate("assistant") })
        }
    ) { padding ->
        val pulseTransition = rememberInfiniteTransition(label = "pulse")
        val pulseScale by pulseTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.03f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))

                // Mega Tournament Banner (Featured)
                val featured = uiState.tournaments.firstOrNull { it.status == TournamentStatus.ONGOING }
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF1A1A1A), Color.Black)
                            )
                        )
                        .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.3f), RoundedCornerShape(32.dp))
                ) {
                    // Golden Glow Effect
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Color(0xFFFFD700).copy(alpha = 0.1f), Color.Transparent),
                                    radius = 400f
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            color = Color(0xFFFFD700),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.scale(pulseScale)
                        ) {
                            Text(
                                "GLOBAL FINALS",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            featured?.title ?: "Continental Scholars Cup",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Ends in: 04d 12h 45m",
                            color = Color(0xFFFFD700),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { /* TODO */ },
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text("ENTER NOW", fontWeight = FontWeight.Black, fontSize = 12.sp)
                        }
                    }
                }
            }

            item {
                Text("ONGOING EVENTS", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(12.dp))
                
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    val ongoing = uiState.tournaments.filter { it.status == TournamentStatus.ONGOING }
                    if (ongoing.isEmpty()) {
                        // Placeholders if no data
                        CompetitionCard(
                            title = "Science Olympiad",
                            subtitle = "Physics & Chemistry Focus",
                            players = "840 Participated",
                            reward = "5,000 XP",
                            icon = Icons.Default.Science,
                            color = Color(0xFF00C853)
                        )
                        CompetitionCard(
                            title = "MIL Masterclass",
                            subtitle = "Fact-Checking Speedrun",
                            players = "1.2k Participated",
                            reward = "Exclusive Badge",
                            icon = Icons.Default.AutoAwesome,
                            color = Color(0xFF00B0FF)
                        )
                    } else {
                        ongoing.forEach { tournament ->
                            CompetitionCard(
                                title = tournament.title,
                                subtitle = tournament.subtitle,
                                players = "${tournament.participantCount} Participated",
                                reward = "${tournament.rewardXp} XP",
                                icon = Icons.Default.EmojiEvents, // Use appropriate icon mapping
                                color = Color(android.graphics.Color.parseColor(tournament.colorHex))
                            )
                        }
                    }
                }
            }

            item {
                Text("UPCOMING TOURNAMENTS", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(12.dp))
                
                val upcoming = uiState.tournaments.filter { it.status == TournamentStatus.UPCOMING }
                if (upcoming.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(24.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Math Whiz Invitational", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Starts Saturday, 10:00 AM (Your Local Time)", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(
                                onClick = { /* TODO */ },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.5f))
                            ) {
                                Text("SET REMINDER", color = GreenPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                } else {
                    upcoming.forEach { tournament ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(24.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(tournament.title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(tournament.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedButton(
                                    onClick = { /* TODO */ },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.5f))
                                ) {
                                    Text("SET REMINDER", color = GreenPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = GreenPrimary.copy(alpha = 0.05f)),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Compete for Prizes", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                            Text("View seasonal rewards", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        }
                        IconButton(
                            onClick = { /* TODO */ },
                            modifier = Modifier.clip(CircleShape).background(GreenPrimary)
                        ) {
                            Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun CompetitionCard(
    title: String,
    subtitle: String,
    players: String,
    reward: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.padding(14.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.People, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(players, color = Color.Gray, fontSize = 10.sp)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("REWARD", color = GreenPrimary, fontSize = 8.sp, fontWeight = FontWeight.Black)
                Text(reward, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompetitionScreenPreview() {
    LxquizTheme {
        CompetitionScreenContent(
            uiState = CompetitionUiState(),
            onNavigate = {},
            onRefresh = {}
        )
    }
}
