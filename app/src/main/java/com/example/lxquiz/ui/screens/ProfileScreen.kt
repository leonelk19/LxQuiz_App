package com.example.lxquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.lxquiz.core.designsystem.BrandingLogo
import com.example.lxquiz.core.designsystem.theme.GreenPrimary
import com.example.lxquiz.core.designsystem.theme.GreenDark
import com.example.lxquiz.core.designsystem.theme.LxquizTheme

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.lxquiz.ui.viewmodels.ProfileUiState
import com.example.lxquiz.ui.viewmodels.ProfileViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onLogout()
        }
    }

    ProfileScreenContent(
        uiState = uiState,
        onNavigate = onNavigate,
        onLogoutClick = { viewModel.logout() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    uiState: ProfileUiState,
    onNavigate: (String) -> Unit,
    onLogoutClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = GreenDark, fontWeight = FontWeight.ExtraBold) },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = GreenDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            BottomNavigationBar(currentScreen = "profile", onNavigate = onNavigate)
        },
        floatingActionButton = {
            AssistantFAB(onClick = { onNavigate("assistant") })
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
                Spacer(modifier = Modifier.height(24.dp))
                
                // Enhanced Profile Avatar
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(GreenPrimary.copy(alpha = 0.05f))
                        .border(2.dp, GreenPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person, 
                        contentDescription = null, 
                        modifier = Modifier.size(60.dp), 
                        tint = GreenPrimary
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text(uiState.profile?.fullName ?: "Learner", color = MaterialTheme.colorScheme.onSurface, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(uiState.profile?.email ?: "loading...", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                
                Surface(
                    modifier = Modifier.padding(top = 12.dp),
                    color = GreenDark.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        "${uiState.profile?.educationLevel?.name ?: "Student"} • ${uiState.profile?.points ?: 0} pts", 
                        color = GreenDark, 
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileStat("45", "Total Quizzes", Modifier.weight(1f))
                    ProfileStat("82%", "Avg Score", Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileStat(uiState.profile?.streak?.toString() ?: "0", "Day Streak", Modifier.weight(1f))
                    ProfileStat("6.4k", "Arena XP", Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(32.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Achievements", color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("View All", color = GreenPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BadgeItem("Quiz Master", Icons.Default.EmojiEvents, GreenPrimary)
                    BadgeItem("Speedster", Icons.Default.FlashOn, Color(0xFFFFA000))
                    BadgeItem("Consistent", Icons.Default.CalendarToday, GreenDark)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Settings Menu
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        MenuItem(Icons.Default.Edit, "Edit Profile")
                        MenuItem(Icons.Default.Notifications, "Notifications")
                        MenuItem(Icons.Default.PrivacyTip, "Privacy Settings")
                        MenuItem(Icons.Default.Download, "Download Data")
                        MenuItem(Icons.AutoMirrored.Filled.Help, "Help & Support")
                        MenuItem(Icons.AutoMirrored.Filled.Logout, "Logout", isLogout = true, onClick = onLogoutClick)
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                BrandingLogo(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp))
            }
        }
    }
}

@Composable
fun BadgeItem(label: String, icon: ImageVector, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.05f))
                .border(1.5.dp, color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, color = MaterialTheme.colorScheme.onSurface, fontSize = 10.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ProfileStat(value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(value, color = GreenDark, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
    }
}

@Composable
fun MenuItem(icon: ImageVector, label: String, isLogout: Boolean = false, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isLogout) Color.Red.copy(alpha = 0.05f) else GreenPrimary.copy(alpha = 0.05f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon, 
                contentDescription = null, 
                tint = if (isLogout) Color.Red else GreenPrimary, 
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            label, 
            color = if (isLogout) Color.Red else MaterialTheme.colorScheme.onSurface, 
            fontSize = 14.sp, 
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        if (!isLogout) {
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    LxquizTheme {
        ProfileScreenContent(
            uiState = ProfileUiState(),
            onNavigate = {},
            onLogoutClick = {}
        )
    }
}
