package com.example.lxquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.ui.components.BottomNavigationBar
import com.example.lxquiz.ui.theme.GreenPrimary
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeScreen(
    onNavigate: (String) -> Unit,
    onGenerateLink: () -> Unit
) {
    var selectedSubject by remember { mutableStateOf("Mathematics") }
    var selectedLevel by remember { mutableStateOf("O/L") }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SportsMartialArts, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Battle Station", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    Button(
                        onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text("CHALLENGE MODE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        bottomBar = {
            BottomNavigationBar(currentScreen = "challenge", onNavigate = onNavigate)
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

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(GreenPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.SportsMartialArts, contentDescription = null, tint = Color.Black)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Create a Battle", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text("AI-powered live competition for your class", color = Color.Gray, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text("SUBJECT", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = selectedSubject,
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color.Black.copy(alpha = 0.2f),
                                unfocusedContainerColor = Color.Black.copy(alpha = 0.2f),
                                unfocusedBorderColor = Color.DarkGray,
                                focusedBorderColor = GreenPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            readOnly = true
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text("GCE LEVEL", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth().height(48.dp)) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                                    .background(if (selectedLevel == "O/L") GreenPrimary.copy(alpha = 0.1f) else Color.Transparent)
                                    .border(1.dp, if (selectedLevel == "O/L") GreenPrimary else Color.DarkGray, RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                                    .clickable { selectedLevel = "O/L" },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("O/L", color = if (selectedLevel == "O/L") GreenPrimary else Color.Gray)
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                                    .background(if (selectedLevel == "A/L") GreenPrimary.copy(alpha = 0.1f) else Color.Transparent)
                                    .border(1.dp, if (selectedLevel == "A/L") GreenPrimary else Color.DarkGray, RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                                    .clickable { selectedLevel = "A/L" },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("A/L", color = if (selectedLevel == "A/L") GreenPrimary else Color.Gray)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ChallengeDropdown("QUESTIONS", "10 Questions", Modifier.weight(1f))
                            ChallengeDropdown("TIME LIMIT", "10 Minutes", Modifier.weight(1f))
                            ChallengeDropdown("PARTICIPANTS", "2 Players", Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Summary bar
                        Box(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color.Black.copy(alpha = 0.2f)).padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("SUBJECT", color = Color.Gray, fontSize = 8.sp)
                                    Text("Mathematics", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Column(modifier = Modifier.weight(0.5f)) {
                                    Text("LEVEL", color = Color.Gray, fontSize = 8.sp)
                                    Text("O/L", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Column(modifier = Modifier.weight(0.7f)) {
                                    Text("QUESTIONS", color = Color.Gray, fontSize = 8.sp)
                                    Text("10", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Column(modifier = Modifier.weight(0.7f)) {
                                    Text("DURATION", color = Color.Gray, fontSize = 8.sp)
                                    Text("10m", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Column(modifier = Modifier.weight(0.7f)) {
                                    Text("PLAYERS", color = Color.Gray, fontSize = 8.sp)
                                    Text("2", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = onGenerateLink,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                        ) {
                            Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate Challenge Link", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // How it works
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("How it works", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        val steps = listOf(
                            "Create" to "Pick subject, level & questions",
                            "Share" to "Send QR code or link to classmates",
                            "Ready Up" to "Everyone clicks Ready to start",
                            "Battle" to "Same AI questions for everyone",
                            "Results" to "Live leaderboard after everyone done"
                        )
                        
                        steps.forEachIndexed { index, (title, desc) ->
                            Row(verticalAlignment = Alignment.Top) {
                                Box(
                                    modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).background(GreenPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text((index + 1).toString(), color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text(desc, color = Color.Gray, fontSize = 12.sp)
                                    if (index < steps.size - 1) Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                // Top Battlers
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("TOP BATTLERS", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        Text("No battles yet", color = Color.Gray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(32.dp))
                        Text("View Full Leaderboard →", color = Color(0xFFFFD700), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ChallengeDropdown(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, color = Color.Gray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Black.copy(alpha = 0.2f),
                unfocusedContainerColor = Color.Black.copy(alpha = 0.2f),
                unfocusedBorderColor = Color.DarkGray
            ),
            shape = RoundedCornerShape(8.dp),
            readOnly = true,
            textStyle = LocalTextStyle.current.copy(fontSize = 10.sp)
        )
    }
}
