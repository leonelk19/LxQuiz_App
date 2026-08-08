package com.example.lxquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.data.models.Country
import com.example.lxquiz.data.models.EducationLevel
import com.example.lxquiz.data.repository.CurriculumRepository
import com.example.lxquiz.core.designsystem.theme.GreenPrimary
import com.example.lxquiz.core.designsystem.BrandingLogo
import java.util.Locale.getDefault

import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import com.example.lxquiz.core.designsystem.theme.LxquizTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onBack: () -> Unit,
    onSignUp: (String, String, Country?, EducationLevel?, String) -> Unit,
    onLogin: () -> Unit,
    error: String? = null
) {
    val repository = remember { CurriculumRepository() }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf<Country?>(null) }
    var selectedLevel by remember { mutableStateOf<EducationLevel?>(null) }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var agreed by remember { mutableStateOf(false) }
    var levelExpanded by remember { mutableStateOf(false) }
    var countryExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                    .size(48.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = GreenPrimary)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Sign Up",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Join LxQuiz and start your professional learning journey.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )

            if (error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            SignUpTextField(label = "Full Name", value = fullName, onValueChange = { fullName = it }, placeholder = "Enter your full name")
            Spacer(modifier = Modifier.height(16.dp))
            SignUpTextField(label = "Email", value = email, onValueChange = { email = it }, placeholder = "ngaralex00@gmail.com")
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Country", color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Box {
                OutlinedTextField(
                    value = selectedCountry?.let { "${it.flagEmoji} ${it.name}" } ?: "",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth().clickable { countryExpanded = true },
                    placeholder = { Text("Select your country", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                    trailingIcon = { 
                        IconButton(onClick = { countryExpanded = !countryExpanded }) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = GreenPrimary)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    readOnly = true,
                    enabled = false
                )
                DropdownMenu(
                    expanded = countryExpanded,
                    onDismissRequest = { countryExpanded = false },
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
                ) {
                    repository.getCountries().forEach { country ->
                        DropdownMenuItem(
                            text = { Text("${country.flagEmoji} ${country.name}", color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                selectedCountry = country
                                countryExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Level", color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Box {
                OutlinedTextField(
                    value = selectedLevel?.name?.lowercase()?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() } ?: "",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth().clickable { levelExpanded = true },
                    placeholder = { Text("Select your level", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                    trailingIcon = { 
                        IconButton(onClick = { levelExpanded = !levelExpanded }) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = GreenPrimary)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    readOnly = true,
                    enabled = false
                )
                DropdownMenu(
                    expanded = levelExpanded,
                    onDismissRequest = { levelExpanded = false },
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
                ) {
                    EducationLevel.entries.forEach { level ->
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    level.name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() },
                                    color = MaterialTheme.colorScheme.onSurface
                                ) 
                            },
                            onClick = {
                                selectedLevel = level
                                levelExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Password", color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("••••••••", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                trailingIcon = { 
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = agreed,
                    onCheckedChange = { agreed = it },
                    colors = CheckboxDefaults.colors(checkedColor = GreenPrimary, uncheckedColor = MaterialTheme.colorScheme.outline)
                )
                Text(
                    text = "I agree to the Terms and Privacy Policy.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onSignUp(fullName, email, selectedCountry, selectedLevel, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                enabled = fullName.isNotBlank() && email.isNotBlank() && selectedCountry != null && selectedLevel != null && password.length >= 6 && agreed
            ) {
                Text("Sign Up", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Already have an account? ", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                TextButton(onClick = onLogin) {
                    Text("Sign In", color = GreenPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SignUpTextField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Column {
        Text(label, color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    LxquizTheme {
        SignUpScreen(onBack = {}, onSignUp = { _, _, _, _, _ -> }, onLogin = {})
    }
}
