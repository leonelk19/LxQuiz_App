package com.example.lxquiz.ui.screens.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.core.designsystem.LxDivider
import com.example.lxquiz.core.designsystem.StepItem
import com.example.lxquiz.core.designsystem.theme.GreenDark
import com.example.lxquiz.core.designsystem.theme.GreenPrimary
import com.example.lxquiz.data.models.EducationLevel
import com.example.lxquiz.data.repository.CurriculumRepository
import com.example.lxquiz.ui.viewmodels.user.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizSetupScreen(
    initialSubject: String? = null,
    onBack: () -> Unit,
    onNextToTopic: (String) -> Unit,
    authViewModel: AuthViewModel
) {
    val repository = remember { CurriculumRepository() }
    val profile = authViewModel.currentUserProfile

    // Retrieve user's saved subjects from sign-up profile or fall back to curriculum default
    val subjects = remember(profile) {
        val enrolled = profile?.selectedSubjects?.filter { it.isNotBlank() } ?: emptyList()
        if (enrolled.isNotEmpty()) {
            enrolled
        } else {
            val country = profile?.countryId?.takeIf { it.isNotBlank() } ?: "US"
            val level = profile?.educationLevel ?: EducationLevel.SECONDARY
            val subLevel = profile?.subLevelId?.takeIf { it.isNotBlank() } ?: "g10"
            val section = profile?.sectionId?.takeIf { it.isNotBlank() } ?: "sci"
            repository.getSubjects(country, level, subLevel, section).map { it.name }
        }
    }

    var selectedSubject by remember { mutableStateOf(initialSubject ?: subjects.firstOrNull()) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Take a Quiz", color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        floatingActionButton = {
            if (selectedSubject != null) {
                ExtendedFloatingActionButton(
                    onClick = {
                        selectedSubject?.let { onNextToTopic(it) }
                    },
                    containerColor = GreenPrimary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    icon = { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null) },
                    text = { Text("Next: Select Topic", fontWeight = FontWeight.Bold) }
                )
            }
        }
    ) { padding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Steps indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
            ) {
                StepItem(1, "Subject", isActive = true, isCompleted = selectedSubject != null)
                LxDivider(modifier = Modifier.weight(1f).padding(horizontal = 6.dp).height(1.dp), color = MaterialTheme.colorScheme.outlineVariant)
                StepItem(2, "Topic", isActive = false, isCompleted = false)
                LxDivider(modifier = Modifier.weight(1f).padding(horizontal = 6.dp).height(1.dp), color = MaterialTheme.colorScheme.outlineVariant)
                StepItem(3, "Quiz", isActive = false, isCompleted = false)
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Subject Selection Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Book, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Select a Subject", color = GreenDark, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Choose a subject from your curriculum", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(20.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.heightIn(max = 320.dp)
                    ) {
                        items(subjects) { subject ->
                            val isSelected = selectedSubject == subject
                            Surface(
                                onClick = { selectedSubject = subject },
                                color = if (isSelected) GreenPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, if (isSelected) GreenPrimary else Color.Transparent, RoundedCornerShape(12.dp))
                            ) {
                                Box(modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = subject,
                                        color = if (isSelected) GreenPrimary else MaterialTheme.colorScheme.onSurface,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
