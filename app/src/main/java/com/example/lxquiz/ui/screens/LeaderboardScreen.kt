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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.core.designsystem.AssistantFAB
import com.example.lxquiz.core.designsystem.BottomNavigationBar
import com.example.lxquiz.core.designsystem.Divider
import com.example.lxquiz.core.designsystem.theme.GreenPrimary
import com.example.lxquiz.core.designsystem.theme.GreenDark
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lxquiz.core.designsystem.theme.LxquizTheme
import com.example.lxquiz.ui.viewmodels.AuthState
import com.example.lxquiz.ui.viewmodels.AuthViewModel

import androidx.compose.runtime.collectAsState
import com.example.lxquiz.ui.viewmodels.CompetitionUiState
import com.example.lxquiz.ui.viewmodels.CompetitionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    onNavigate: (String) -> Unit,
    authViewModel: AuthViewModel,
    competitionViewModel: CompetitionViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val competitionState by competitionViewModel.uiState.collectAsState()
    
    val userCountry = (authState as? AuthState.Success)?.profile?.countryId?.uppercase() ?: "Cameroon"

    LeaderboardScreenContent(
        uiState = competitionState,
        userCountry = userCountry,
        onNavigate = onNavigate,
        onTabSelected = { competitionViewModel.selectTab(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreenContent(
    uiState: CompetitionUiState,
    userCountry: String,
    onNavigate: (String) -> Unit,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf("Global", "Continental", "National")
    var regionExpanded by remember { mutableStateOf(false) }
    
    var selectedRegion by remember { mutableStateOf("Africa") }
    
    val continentalRegions = listOf("Africa", "Americas", "Asia", "Europe", "Oceania")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Leaderboard", color = GreenDark, fontWeight = FontWeight.ExtraBold) },
                actions = {
                    if (uiState.selectedTab == 1) {
                        Box {
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                onClick = { regionExpanded = true }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(selectedRegion, color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)
                                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = GreenDark, modifier = Modifier.size(16.dp))
                                }
                            }
                            DropdownMenu(
                                expanded = regionExpanded,
                                onDismissRequest = { regionExpanded = false },
                                modifier = Modifier.background(MaterialTheme.colorScheme.surface).heightIn(max = 400.dp)
                            ) {
                                continentalRegions.forEach { continent ->
                                    DropdownMenuItem(
                                        text = { Text(continent) },
                                        onClick = {
                                            selectedRegion = continent
                                            regionExpanded = false
                                            onTabSelected(1) // Trigger reload with region
                                        }
                                    )
                                }
                            }
                        }
                    } else if (uiState.selectedTab == 2) {
                         Surface(
                            color = GreenPrimary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.2f))
                        ) {
                            Text(
                                userCountry, 
                                color = GreenPrimary, 
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                Button(
                    onClick = { onNavigate("competition") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Icon(Icons.Default.Stars, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Join Active Competition", fontWeight = FontWeight.Bold, color = Color.White)
                }
                BottomNavigationBar(currentScreen = "leaderboard", onNavigate = onNavigate)
            }
        },
        floatingActionButton = {
            AssistantFAB(onClick = { onNavigate("assistant") })
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
                
                // Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    tabs.forEachIndexed { index, title ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (uiState.selectedTab == index) GreenPrimary else Color.Transparent)
                                .clickable { onTabSelected(index) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                color = if (uiState.selectedTab == index) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                CurrentUserRankCard()
                Spacer(modifier = Modifier.height(24.dp))
                Text("Top Rankings", color = GreenDark, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (uiState.isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = GreenPrimary)
                    }
                }
            } else if (uiState.leaderboard.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text("No rankings available", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                itemsIndexed(uiState.leaderboard) { index, player ->
                    RankItem(index + 1, player.userName, "${player.points} pts")
                    if (index < uiState.leaderboard.size - 1) {
                        Divider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.padding(vertical = 8.dp))
                    }
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
        colors = CardDefaults.cardColors(containerColor = GreenPrimary.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("#47", color = GreenPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.LightGray))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Alex Johnson (You)", color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("+150 pts last week", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            }
            Text("2,150 pts", color = GreenPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RankItem(rank: Int, name: String, points: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(rank.toString(), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant))
        Spacer(modifier = Modifier.width(16.dp))
        Text(name, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Text(points, color = GreenPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenPreview() {
    LxquizTheme {
        LeaderboardScreenContent(
            uiState = CompetitionUiState(),
            userCountry = "Cameroon",
            onNavigate = {},
            onTabSelected = {}
        )
    }
}
