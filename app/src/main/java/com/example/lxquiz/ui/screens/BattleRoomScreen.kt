package com.example.lxquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.ui.theme.GreenPrimary
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleRoomScreen(
    onBack: () -> Unit,
    onBattleStart: () -> Unit
) {
    var isReady by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFF000033), // Deep blue for room
        topBar = {
            TopAppBar(
                title = { Text("Battle Room", color = Color.White, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF000033), Color.Black)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    color = Color.Red.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                        Box(modifier = Modifier.size(8.dp).clip(androidx.compose.foundation.shape.CircleShape).background(Color.Red))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Connecting...", color = Color.Red, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Icon(Icons.Default.SportsMartialArts, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Battle Room #O9SMEZ", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Mathematics • O/L • 10 questions", color = Color.Gray, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(48.dp))

                Card(
                    modifier = Modifier.width(280.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Default.Group, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Players in Room (0)", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.weight(1f))
                            Text("0/2 Ready", color = Color.Gray, fontSize = 10.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(48.dp))
                        
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFE91E63).copy(alpha = 0.3f), modifier = Modifier.size(48.dp))
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("No players yet", color = Color.Gray, fontSize = 12.sp)
                        Text("Waiting to connect...", color = Color.Gray.copy(alpha = 0.5f), fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = { 
                        isReady = !isReady
                        if(isReady) {
                             // Logic to start battle could be here
                        }
                    },
                    modifier = Modifier.width(280.dp).height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isReady) Color(0xFF00C853) else GreenPrimary
                    )
                ) {
                    if (isReady) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("I'm Ready!", color = Color.White, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.FlashOn, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("I'm Ready to Battle!", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
