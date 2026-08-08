package com.example.lxquiz.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.core.designsystem.AssistantFAB
import com.example.lxquiz.core.designsystem.BottomNavigationBar
import com.example.lxquiz.core.designsystem.theme.GreenDark
import com.example.lxquiz.core.designsystem.theme.GreenPrimary
import com.example.lxquiz.core.designsystem.theme.GreenSecondary
import com.example.lxquiz.core.designsystem.theme.LxquizTheme
import com.example.lxquiz.data.models.ArenaConfig
import com.example.lxquiz.data.models.UserProfile
import com.example.lxquiz.ui.viewmodels.ArenaUiState
import com.example.lxquiz.ui.viewmodels.ArenaViewModel
import com.example.lxquiz.ui.viewmodels.AuthState
import com.example.lxquiz.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeScreen(
    onNavigate: (String) -> Unit,
    onGenerateLink: () -> Unit,
    viewModel: ArenaViewModel,
    authViewModel: AuthViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val authState by authViewModel.authState.collectAsState()

    // Handle navigation when room is created
    LaunchedEffect(uiState.currentRoom) {
        if (uiState.currentRoom != null) {
            onGenerateLink()
        }
    }

    ChallengeScreenContent(
        uiState = uiState,
        authState = authState,
        onNavigate = onNavigate,
        onUpdateConfig = { update -> viewModel.updateConfig(update) },
        onInitiateArena = { profile -> viewModel.initiateArena(profile) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeScreenContent(
    uiState: ArenaUiState,
    authState: AuthState,
    onNavigate: (String) -> Unit,
    onUpdateConfig: ((ArenaConfig) -> ArenaConfig) -> Unit,
    onInitiateArena: (UserProfile) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "LX ARENA", 
                            color = GreenPrimary, 
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = Color.Red,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                "LIVE", 
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.History, contentDescription = null, tint = GreenPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            BottomNavigationBar(currentScreen = "challenge", onNavigate = onNavigate)
        },
        floatingActionButton = {
            AssistantFAB(onClick = { onNavigate("assistant") })
        }
    ) { padding ->
        val pulseTransition = rememberInfiniteTransition(label = "pulse")
        val pulseScale by pulseTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.05f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    // Super Hero Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(GreenSecondary, GreenDark)
                                )
                            )
                    ) {
                        // Decorative Elements
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.1f),
                            modifier = Modifier
                                .size(180.dp)
                                .align(Alignment.CenterEnd)
                                .offset(x = 40.dp)
                        )

                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Whatshot, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("ARENA SEASON 1", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("The Ultimate\nKnowledge Battle", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 28.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF00E676)))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("1,248 Students Online", color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Configuration Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(32.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text("BATTLE PARAMETERS", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(16.dp))

                            BattleInput(
                                label = "Subject Area",
                                value = uiState.config.subjectName,
                                icon = Icons.AutoMirrored.Filled.MenuBook,
                                onClick = { /* TODO: Open Subject Picker */ }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                BattleInput(
                                    label = "Level",
                                    value = uiState.config.level.name,
                                    icon = Icons.Default.School,
                                    modifier = Modifier.weight(1f),
                                    onClick = { /* TODO: Open Level Picker */ }
                                )
                                BattleInput(
                                    label = "Grade",
                                    value = uiState.config.subLevelName ?: "Select",
                                    icon = Icons.Default.WorkspacePremium,
                                    modifier = Modifier.weight(1f),
                                    onClick = { /* TODO: Open SubLevel Picker */ }
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                BattleInput(
                                    label = "Section",
                                    value = uiState.config.sectionName ?: "General",
                                    icon = Icons.Default.Category,
                                    modifier = Modifier.weight(1f),
                                    onClick = { /* TODO: Open Section Picker */ }
                                )
                                BattleInput(
                                    label = "Questions",
                                    value = "${uiState.config.questionCount}",
                                    icon = Icons.Default.Quiz,
                                    modifier = Modifier.weight(1f),
                                    onClick = { 
                                        onUpdateConfig { it.copy(questionCount = if(it.questionCount >= 25) 5 else it.questionCount + 5) }
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text("VICTORY STAKES", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                StakeChip(
                                    label = "Standard",
                                    multiplier = "1.0x",
                                    isSelected = uiState.config.stakesMultiplier == 1.0f,
                                    onClick = { onUpdateConfig { it.copy(stakesMultiplier = 1.0f) } },
                                    modifier = Modifier.weight(1f)
                                )
                                StakeChip(
                                    label = "High XP",
                                    multiplier = "1.5x",
                                    isSelected = uiState.config.stakesMultiplier == 1.5f,
                                    onClick = { onUpdateConfig { it.copy(stakesMultiplier = 1.5f) } },
                                    modifier = Modifier.weight(1f)
                                )
                                StakeChip(
                                    label = "Legendary",
                                    multiplier = "2.0x",
                                    isSelected = uiState.config.stakesMultiplier == 2.0f,
                                    onClick = { onUpdateConfig { it.copy(stakesMultiplier = 2.0f) } },
                                    modifier = Modifier.weight(1f),
                                    isHot = true
                                )
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            Button(
                                onClick = { 
                                    val profile = (authState as? AuthState.Success)?.profile ?: UserProfile(fullName = "Alex Johnson")
                                    onInitiateArena(profile) 
                                },
                                enabled = !uiState.isLoading,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                                    .scale(if (!uiState.isLoading) pulseScale else 1f),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                                } else {
                                    Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        "LAUNCH BATTLE ROOM", 
                                        fontWeight = FontWeight.ExtraBold, 
                                        color = Color.White,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Cross-Origin: The AI will sync curricula between you and your opponent automatically.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 10.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("ACTIVE ARENAS", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                        Surface(
                            color = Color(0xFF00E676).copy(alpha = 0.1f),
                            shape = CircleShape
                        ) {
                            Text(
                                "42 ACTIVE", 
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                color = Color(0xFF00E676),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.Red))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Global Matchmaking", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                                    }
                                    Text("Join a public room instantly", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                                }
                                Button(
                                    onClick = { /* TODO */ },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                                ) {
                                    Text("FIND MATCH", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // High Stakes Arena Promo
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Black,
                            shape = RoundedCornerShape(24.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Elite Tournament", color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                    Text("2.5x XP • Ends in 2h 45m", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                                }
                                TextButton(
                                    onClick = { /* TODO */ },
                                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFFD700))
                                ) {
                                    Text("ENTER", fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                }

                item {
                    Text("BATTLE MASTERPLAN", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        BattleStep(1, "Dominate Selection", "Define your subject and victory stakes")
                        BattleStep(2, "Recruit Rivals", "Invite peers via localized arena links")
                        BattleStep(3, "Cross-Origin Sync", "AI balances both curricula into a unique quiz")
                        BattleStep(4, "Claim Glory", "Win the battle to multiply your Arena XP")
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun StakeChip(
    label: String,
    multiplier: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    isHot: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) {
            if (isHot) Color(0xFFFF3D00).copy(alpha = 0.1f) else GreenPrimary.copy(alpha = 0.1f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) {
                if (isHot) Color(0xFFFF3D00) else GreenPrimary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                multiplier, 
                color = if (isHot) Color(0xFFFF3D00) else GreenPrimary, 
                fontSize = 14.sp, 
                fontWeight = FontWeight.Black
            )
            Text(
                label, 
                color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Gray, 
                fontSize = 8.sp, 
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BattleInput(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            color = Color.Gray,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { onClick() },
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(value, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.ExpandMore, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun BattleStep(number: Int, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(GreenPrimary.copy(alpha = 0.1f))
                .border(1.dp, GreenPrimary.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(number.toString(), color = GreenPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(desc, color = Color.Gray, fontSize = 11.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChallengeScreenPreview() {
    LxquizTheme {
        ChallengeScreenContent(
            uiState = ArenaUiState(),
            authState = AuthState.Idle,
            onNavigate = {},
            onUpdateConfig = {},
            onInitiateArena = {}
        )
    }
}
