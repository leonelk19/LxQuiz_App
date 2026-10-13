package com.example.lxquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.ui.components.StepItem
import com.example.lxquiz.ui.components.Divider
import com.example.lxquiz.ui.theme.GreenPrimary
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldOfStudyScreen(
    onBack: () -> Unit,
    onContinueToSubjects: (String) -> Unit
) {
    var selectedField by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("Take a Quiz", color = Color.White, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    Surface(
                        color = GreenPrimary,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            "JS", 
                            color = Color.Black, 
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(24.dp)) {
                Button(
                    onClick = { selectedField?.let { onContinueToSubjects(it) } },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = selectedField != null,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenPrimary,
                        disabledContainerColor = Color.DarkGray
                    )
                ) {
                    Text("Continue to Subject Selection", fontWeight = FontWeight.Bold, color = if(selectedField != null) Color.Black else Color.White)
                }
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
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "Choose Your Field of Study",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                "Select your branch of study to see relevant quiz categories tailored to your academic background.",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                StepItem(1, "Choose Field", true)
                Spacer(modifier = Modifier.width(8.dp))
                Divider(modifier = Modifier.width(24.dp).height(1.dp), color = Color.DarkGray)
                Spacer(modifier = Modifier.width(8.dp))
                StepItem(2, "Select Subject", false)
                Spacer(modifier = Modifier.width(8.dp))
                Divider(modifier = Modifier.width(24.dp).height(1.dp), color = Color.DarkGray)
                Spacer(modifier = Modifier.width(8.dp))
                StepItem(3, "Start Quiz", false)
            }

            Spacer(modifier = Modifier.height(32.dp))

            val fields = listOf(
                FieldData("Technical", "Mechanical, Electrical, Civil engineering", Icons.Default.Settings, Color(0xFFC2FF3D)),
                FieldData("Commercial", "Office Practice, Accounting, Marketing, Business Math, Commerce", Icons.Default.Store, Color(0xFFFFD700)),
                FieldData("Sciences", "Physics, Chemistry, Biology, Mathematics", Icons.Default.Science, Color(0xFF00C853)),
                FieldData("Arts", "Literature, History, Philosophy, Languages", Icons.Default.Palette, Color(0xFFE91E63))
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(fields) { field ->
                    FieldCard(
                        field, 
                        isSelected = selectedField == field.title,
                        onClick = { selectedField = field.title }
                    )
                }
            }
        }
    }
}

data class FieldData(val title: String, val description: String, val icon: ImageVector, val color: Color)

@Composable
fun FieldCard(field: FieldData, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, if (isSelected) GreenPrimary else Color.Transparent, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(field.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(field.icon, contentDescription = null, tint = field.color)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(field.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(field.description, color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Explore Subjects", color = GreenPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
