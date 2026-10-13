package com.example.lxquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.ui.components.BottomNavigationBar
import com.example.lxquiz.ui.components.Divider
import com.example.lxquiz.ui.theme.GreenPrimary

@Composable
fun ResultScreen(
    onHome: () -> Unit,
    onReview: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Quiz Complete!", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Icon(
                    Icons.Default.AutoAwesome, 
                    contentDescription = null, 
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        },
        bottomBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Share Results", color = Color.White)
                    }
                    Button(
                        onClick = onHome,
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                    ) {
                        Text("Try Another Quiz", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
                BottomNavigationBar(currentScreen = "home", onNavigate = onNavigate)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { 0.84f },
                    modifier = Modifier.size(160.dp),
                    color = GreenPrimary,
                    strokeWidth = 12.dp,
                    trackColor = Color.DarkGray
                )
                Text("84%", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Excellent!", color = Color(0xFF00C853), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("You answered 21 out of 25 correct", color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ResultMetric(Icons.Default.CheckCircle, "21", "Correct", Color(0xFF00C853), Modifier.weight(1f))
                ResultMetric(Icons.Default.Cancel, "4", "Wrong", Color(0xFFFF3D00), Modifier.weight(1f))
                ResultMetric(Icons.Default.Info, "0", "Skipped", Color.Gray, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ResultRow(Icons.Default.Timer, "Time Taken", "5:32")
                    Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 12.dp))
                    ResultRow(Icons.Default.Percent, "Your Score", "84%")
                    Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 12.dp))
                    ResultRow(Icons.Default.Leaderboard, "Your Rank", "#47")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedCard(
                onClick = onReview,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent, contentColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00C853))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Review Answers", modifier = Modifier.weight(1f))
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun ResultMetric(icon: ImageVector, value: String, label: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Color.Gray, fontSize = 10.sp)
        }
    }
}

@Composable
fun ResultRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
