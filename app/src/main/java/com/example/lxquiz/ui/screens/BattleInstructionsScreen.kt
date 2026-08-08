package com.example.lxquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleInstructionsScreen(
    onBack: () -> Unit,
    onEnterRoom: () -> Unit
) {
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("Battle Setup", color = Color.White, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Icon(Icons.Default.SportsMartialArts, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(64.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Your Battle is Ready!", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Complete these steps before entering the room", color = Color.Gray, fontSize = 14.sp)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Summary Bar
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFF1E1E1E)).padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        InstructionMetric("SUBJECT", "Mathematics", Modifier.weight(1f))
                        InstructionMetric("LEVEL", "O/L", Modifier.weight(0.5f))
                        InstructionMetric("QUESTIONS", "10", Modifier.weight(0.7f))
                        InstructionMetric("DURATION", "10m", Modifier.weight(0.7f))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                InstructionStep(
                    number = 1,
                    title = "Enable your Hotspot",
                    description = "Turn on your phone or laptop hotspot. All players must connect to the same network as you.",
                    actionContent = {
                        Box(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(Color.Black.copy(alpha = 0.3f)).padding(12.dp)
                        ) {
                            Text("Run Django as: python manage.py runserver 0.0.0.0:8000", color = GreenPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                InstructionStep(
                    number = 2,
                    title = "Share the Battle Link",
                    description = "Share the QR code or copy the link. Players open it in their browser on the same network.",
                    actionContent = {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp)).background(Color.Black.copy(alpha = 0.3f)).padding(12.dp)
                            ) {
                                Text("http://192.168.1.100:8000/competition/room/O9SMEZ", color = GreenPrimary, fontSize = 10.sp, maxLines = 1)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { /* TODO */ },
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copy", color = Color.Black, fontSize = 12.sp)
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                InstructionStep(
                    number = 3,
                    title = "Everyone Clicks Ready",
                    description = "In the battle room, every player must click 'I'm Ready'. The countdown starts only when all players are ready. No one gets left behind!",
                    actionContent = {
                         Row(verticalAlignment = Alignment.CenterVertically) {
                             Column {
                                 Text("ROOM ID", color = Color.Gray, fontSize = 8.sp)
                                 Text("O9SMEZ", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                             }
                             Spacer(modifier = Modifier.width(24.dp))
                             Column {
                                 Text("BATTLE ID", color = Color.Gray, fontSize = 8.sp)
                                 Text("#1", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                             }
                             Spacer(modifier = Modifier.weight(1f))
                             Surface(color = Color(0xFF00C853).copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                                 Text("+ READY", color = Color(0xFF00C853), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                             }
                         }
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onEnterRoom,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("Enter Battle Room →", fontWeight = FontWeight.Bold, color = Color.Black)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun InstructionMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, color = Color.Gray, fontSize = 8.sp)
        Text(value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun InstructionStep(number: Int, title: String, description: String, actionContent: @Composable () -> Unit) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(GreenPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(number.toString(), color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(12.dp))
            actionContent()
        }
    }
}
