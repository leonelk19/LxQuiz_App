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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.core.designsystem.AssistantFAB
import com.example.lxquiz.core.designsystem.BottomNavigationBar
import com.example.lxquiz.core.designsystem.theme.GreenPrimary
import com.example.lxquiz.core.designsystem.theme.GreenDark

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

import androidx.compose.material3.MaterialTheme
import com.example.lxquiz.core.designsystem.theme.LxquizTheme

import androidx.compose.runtime.collectAsState
import com.example.lxquiz.ui.viewmodels.AnalyticsUiState
import com.example.lxquiz.ui.viewmodels.AnalyticsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onNavigate: (String) -> Unit,
    viewModel: AnalyticsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    AnalyticsScreenContent(
        uiState = uiState,
        onNavigate = onNavigate,
        onRefresh = { viewModel.loadStats() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreenContent(
    uiState: AnalyticsUiState,
    onNavigate: (String) -> Unit,
    onRefresh: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Overall", "Normal Quizzes", "Competitions")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Analytics", color = GreenDark, fontWeight = FontWeight.ExtraBold) },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = GreenDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            BottomNavigationBar(currentScreen = "analytics", onNavigate = onNavigate)
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
                                .background(if (selectedTab == index) GreenPrimary else Color.Transparent)
                                .clickable { selectedTab = index }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                color = if (selectedTab == index) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Cards
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AnalyticsStatCard(
                        value = uiState.stats.totalQuizzesTaken.toString(), 
                        label = "Total Taken", 
                        icon = Icons.Default.Description, 
                        subtext = "+ Accumulated", 
                        iconColor = GreenPrimary, 
                        modifier = Modifier.weight(1f)
                    )
                    AnalyticsStatCard(
                        value = String.format(Locale.US, "%.0f%%", uiState.stats.averageScore), 
                        label = "Average Score", 
                        icon = Icons.Default.PieChart, 
                        subtext = "+ Based on results", 
                        iconColor = GreenPrimary, 
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AnalyticsStatCard(
                        value = String.format(Locale.US, "%.1fh", uiState.stats.totalTimeSpentHours), 
                        label = "Time Spent", 
                        icon = Icons.Default.Timer, 
                        subtext = "+ Estimated", 
                        iconColor = GreenPrimary, 
                        modifier = Modifier.weight(1f)
                    )
                    AnalyticsStatCard("AI Tip", "Retake quizzes regularly to improve your rank.", Icons.Default.EmojiObjects, "Strategic MIL", GreenPrimary, Modifier.weight(1f), isAiTip = true)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Score Trend Chart Placeholder
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Score Trend", color = GreenDark, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Row(
                                modifier = Modifier.background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)).padding(4.dp)
                            ) {
                                listOf("Week", "Month", "All").forEach { 
                                     Text(it, color = if(it=="Week") GreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(150.dp).background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))) {
                            if (uiState.stats.scoreTrend.isEmpty()) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("No trend data", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("By Subject", color = GreenDark, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(24.dp))
                            if (uiState.stats.subjectPerformance.isEmpty()) {
                                Text("No data available", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                            } else {
                                // Subject breakdown
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.weight(0.7f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Overall", color = GreenDark, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    progress = { uiState.stats.averageScore / 100f },
                                    modifier = Modifier.size(80.dp),
                                    color = GreenPrimary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                    strokeWidth = 8.dp
                                )
                                Text(String.format(Locale.US, "%.0f%%", uiState.stats.averageScore), color = GreenDark, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun AnalyticsStatCard(value: String, label: String, icon: ImageVector, subtext: String, iconColor: Color, modifier: Modifier = Modifier, isAiTip: Boolean = false) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(16.dp))
                if (isAiTip) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(value, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (isAiTip) {
                Text(label, color = Color.Gray, fontSize = 10.sp, lineHeight = 14.sp)
            } else {
                Text(value, color = MaterialTheme.colorScheme.onSurface, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(label, color = Color.Gray, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(subtext, color = GreenPrimary, fontSize = 10.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnalyticsScreenPreview() {
    LxquizTheme {
        AnalyticsScreenContent(
            uiState = AnalyticsUiState(),
            onNavigate = {},
            onRefresh = {}
        )
    }
}
