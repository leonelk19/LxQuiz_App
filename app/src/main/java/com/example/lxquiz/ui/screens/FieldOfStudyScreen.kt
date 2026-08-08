package com.example.lxquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.lxquiz.core.designsystem.Divider
import com.example.lxquiz.core.designsystem.StepItem
import com.example.lxquiz.core.designsystem.theme.GreenPrimary
import com.example.lxquiz.core.designsystem.theme.LxquizTheme
import com.example.lxquiz.data.models.EducationLevel
import com.example.lxquiz.data.repository.CurriculumRepository
import com.example.lxquiz.ui.viewmodels.AuthState
import com.example.lxquiz.ui.viewmodels.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.filled.AutoAwesome

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldOfStudyScreen(
    onBack: () -> Unit,
    onContinueToSubjects: (String) -> Unit,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.collectAsState()
    val repository = remember { CurriculumRepository() }
    
    val profile = (authState as? AuthState.Success)?.profile
    
    val fields = remember(profile) {
        if (profile != null) {
            repository.getSections(
                profile.countryId,
                profile.educationLevel ?: EducationLevel.SECONDARY,
                profile.subLevelId
            ).map { section ->
                val pair = when (section.id) {
                    "sci", "sci_m", "sci_nm", "stem" -> Pair(Icons.Default.Science, Color(0xFF00C853))
                    "art", "hum", "arts" -> Pair(Icons.Default.Palette, Color(0xFFE91E63))
                    "com", "comm", "soc" -> Pair(Icons.Default.Store, Color(0xFFFFD700))
                    "tech" -> Pair(Icons.Default.Settings, Color(0xFFC2FF3D))
                    else -> Pair(Icons.Default.AutoAwesome, Color(0xFF00B0FF))
                }
                FieldData(
                    title = section.name,
                    description = "Assessments for ${section.name}",
                    icon = pair.first,
                    color = pair.second
                )
            }
        } else {
            listOf(
                FieldData("Sciences", "Physics, Chemistry, Biology, Mathematics", Icons.Default.Science, Color(0xFF00C853)),
                FieldData("Arts & Humanities", "Literature, History, Philosophy, Languages", Icons.Default.Palette, Color(0xFFE91E63)),
                FieldData("Commercial", "Office Practice, Accounting, Marketing, Business Math, Commerce", Icons.Default.Store, Color(0xFFFFD700)),
                FieldData("Technical", "Mechanical, Electrical, Civil engineering", Icons.Default.Settings, Color(0xFFC2FF3D))
            )
        }
    }

    FieldOfStudyScreenContent(
        fields = fields,
        onBack = onBack,
        onContinueToSubjects = onContinueToSubjects
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldOfStudyScreenContent(
    fields: List<FieldData>,
    onBack: () -> Unit,
    onContinueToSubjects: (String) -> Unit
) {
    var selectedField by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Take a Quiz", color = MaterialTheme.colorScheme.onBackground, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
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
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text("Continue to Subject Selection", fontWeight = FontWeight.Bold, color = if(selectedField != null) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
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
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                "Select your branch of study to see relevant quiz categories tailored to your academic background.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                Divider(modifier = Modifier.width(24.dp).height(1.dp), color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.width(8.dp))
                StepItem(2, "Select Subject", false)
                Spacer(modifier = Modifier.width(8.dp))
                Divider(modifier = Modifier.width(24.dp).height(1.dp), color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.width(8.dp))
                StepItem(3, "Start Quiz", false)
            }

            Spacer(modifier = Modifier.height(32.dp))

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
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, if (isSelected) GreenPrimary else Color.Transparent, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
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
                Text(field.title, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(field.description, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Explore Subjects", color = GreenPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FieldOfStudyScreenPreview() {
    LxquizTheme {
        FieldOfStudyScreenContent(
            fields = listOf(
                FieldData("Sciences", "Physics, Chemistry, Biology, Mathematics", Icons.Default.Science, Color(0xFF00C853)),
                FieldData("Arts & Humanities", "Literature, History, Philosophy, Languages", Icons.Default.Palette, Color(0xFFE91E63)),
                FieldData("Commercial", "Office Practice, Accounting, Marketing, Business Math, Commerce", Icons.Default.Store, Color(0xFFFFD700)),
                FieldData("Technical", "Mechanical, Electrical, Civil engineering", Icons.Default.Settings, Color(0xFFC2FF3D))
            ),
            onBack = {},
            onContinueToSubjects = {}
        )
    }
}
