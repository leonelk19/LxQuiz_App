package com.example.lxquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.data.models.*
import com.example.lxquiz.data.repository.CurriculumRepository
import com.example.lxquiz.core.designsystem.theme.GreenPrimary
import com.example.lxquiz.core.designsystem.theme.LxquizTheme
import java.util.Locale.getDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurriculumSelectionScreen(
    initialCountryId: String?,
    initialLevel: EducationLevel?,
    isLoading: Boolean,
    onComplete: (String, EducationLevel, String, String?, List<String>) -> Unit
) {
    val repository = remember { CurriculumRepository() }
    
    var selectedCountry by remember { mutableStateOf(repository.getCountries().find { it.id == initialCountryId }) }
    var selectedLevel by remember { mutableStateOf(initialLevel) }
    var selectedSubLevel by remember { mutableStateOf<SubLevel?>(null) }
    var selectedSection by remember { mutableStateOf<EducationSection?>(null) }
    var selectedSubjects by remember { mutableStateOf(setOf<String>()) }

    var step by remember { 
        mutableIntStateOf(
            when {
                selectedCountry == null -> 1
                selectedLevel == null -> 2
                else -> 3
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = { Text("Personalize Path", color = MaterialTheme.colorScheme.onBackground) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
            ) {
                Text(
                    text = when(step) {
                        1 -> "Which country are you from?"
                        2 -> "What is your education level?"
                        3 -> "Which class/grade are you in?"
                        4 -> "Choose your field of study"
                        else -> "Choose your subjects"
                    },
                    color = GreenPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(24.dp))

                when (step) {
                    1 -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(repository.getCountries()) { country ->
                                SelectionCard(
                                    label = "${country.flagEmoji}  ${country.name}",
                                    isSelected = selectedCountry == country,
                                    onClick = { 
                                        selectedCountry = country
                                        step = 2
                                    }
                                )
                            }
                        }
                    }
                    2 -> {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            EducationLevel.entries.forEach { level ->
                                SelectionCard(
                                    label = level.name.lowercase().replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString()
                                    },
                                    isSelected = selectedLevel == level,
                                    onClick = { 
                                        selectedLevel = level
                                        step = 3
                                    }
                                )
                            }
                        }
                    }
                    3 -> {
                        val subLevels = repository.getSubLevels(selectedCountry?.id ?: "", selectedLevel ?: EducationLevel.SECONDARY)
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(subLevels) { subLevel ->
                                SelectionCard(
                                    label = subLevel.name,
                                    isSelected = selectedSubLevel == subLevel,
                                    onClick = {
                                        selectedSubLevel = subLevel
                                        val sections = repository.getSections(
                                            selectedCountry?.id ?: "",
                                            selectedLevel ?: EducationLevel.SECONDARY,
                                            subLevel.id
                                        )
                                        if (sections.isNotEmpty()) {
                                            step = 4
                                        } else {
                                            step = 5
                                        }
                                    }
                                )
                            }
                        }
                    }
                    4 -> {
                        val sections = repository.getSections(
                            selectedCountry?.id ?: "",
                            selectedLevel ?: EducationLevel.SECONDARY,
                            selectedSubLevel?.id ?: ""
                        )
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(sections) { section ->
                                SelectionCard(
                                    label = section.name,
                                    isSelected = selectedSection == section,
                                    onClick = {
                                        selectedSection = section
                                        step = 5
                                    }
                                )
                            }
                        }
                    }
                    5 -> {
                        val subjects = repository.getSubjects(
                            selectedCountry?.id ?: "", 
                            selectedLevel ?: EducationLevel.SECONDARY,
                            selectedSubLevel?.id ?: "",
                            selectedSection?.id
                        )
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(subjects) { subject ->
                                SelectionCard(
                                    label = subject.name,
                                    isSelected = selectedSubjects.contains(subject.id),
                                    onClick = {
                                        selectedSubjects = if (selectedSubjects.contains(subject.id)) {
                                            selectedSubjects - subject.id
                                        } else {
                                            selectedSubjects + subject.id
                                        }
                                    }
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = { 
                                onComplete(
                                    selectedCountry?.id ?: "", 
                                    selectedLevel ?: EducationLevel.SECONDARY, 
                                    selectedSubLevel?.id ?: "",
                                    selectedSection?.id,
                                    selectedSubjects.toList()
                                )
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                            shape = RoundedCornerShape(12.dp),
                            enabled = selectedSubjects.isNotEmpty() && !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                            } else {
                                Text("Finish Setup", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GreenPrimary)
            }
        }
    }
}

@Composable
fun SelectionCard(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = if (isSelected) GreenPrimary else MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) GreenPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = if (isSelected) GreenPrimary else MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
            if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = GreenPrimary)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurriculumSelectionScreenPreview() {
    LxquizTheme {
        CurriculumSelectionScreen(initialCountryId = "cm", initialLevel = EducationLevel.SECONDARY, isLoading = false, onComplete = { _, _, _, _, _ -> })
    }
}
