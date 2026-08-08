package com.example.lxquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.ui.components.BottomNavigationBar
import com.example.lxquiz.ui.components.Divider
import com.example.lxquiz.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    onNavigate: (String) -> Unit
) {
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("Leaderboard", color = Color.White, fontWeight = FontWeight.Bold) },
                actions = {
                    Surface(
                        color = Color(0xFF1E1E1E),
                        shape = RoundedCornerShape(12.dp),
                        onClick = { /* TODO */ }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Global", color = Color.White, fontSize = 12.sp)
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        bottomBar = {
            Column {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Icon(Icons.Default.Stars, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Join Active Competition", fontWeight = FontWeight.Bold, color = Color.Black)
                }
                BottomNavigationBar(currentScreen = "leaderboard", onNavigate = onNavigate)
            }
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
                CurrentUserRankCard()
                Spacer(modifier = Modifier.height(24.dp))
                Text("Top Rankings", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
            }

            val players = listOf(
                Player("John Smith", "5,240 pts"),
                Player("Jane Doe", "4,890 pts"),
                Player("Alex Brown", "4,650 pts"),
                Player("Maria Garcia", "3,920 pts"),
                Player("Tom Wilson", "3,450 pts")
            )

            itemsIndexed(players) { index, player ->
                RankItem(index + 1, player.name, player.points)
                if (index < players.size - 1) {
                    Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 8.dp))
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

data class Player(val name: String, val points: String)

@Composable
fun CurrentUserRankCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = GreenPrimary),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("#47", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.Black.copy(alpha = 0.2f)))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Alex Johnson (You)", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("+150 pts last week", color = Color.Black.copy(alpha = 0.8f), fontSize = 12.sp)
            }
            Text("2,150 pts", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RankItem(rank: Int, name: String, points: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(rank.toString(), color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.DarkGray))
        Spacer(modifier = Modifier.width(16.dp))
        Text(name, color = Color.White, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Text(points, color = GreenPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
