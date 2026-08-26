package com.example.lxquiz.ui.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxquiz.R
import com.example.lxquiz.core.designsystem.LxQuizLogo
import com.example.lxquiz.core.designsystem.theme.GreenPrimary
import com.example.lxquiz.core.designsystem.theme.LxquizTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun WelcomeScreen(
    onStartJourney: () -> Unit,
    onLogin: () -> Unit,
    onSignUp: () -> Unit,
    onGoogleSignIn: (String) -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                account?.idToken?.let { idToken ->
                    onGoogleSignIn(idToken)
                }
            } catch (e: ApiException) {
                // Handle error
            }
        }
    }
    
    LaunchedEffect(Unit) {
        delay(300.milliseconds)
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Subtle Green Background Element
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-100).dp)
                .blur(80.dp)
                .alpha(0.1f)
                .background(Brush.radialGradient(listOf(GreenPrimary, Color.Transparent)))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(1000)) + expandVertically(tween(1000))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LxQuizLogo(modifier = Modifier.size(180.dp))
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "LxQuiz",
                        color = GreenPrimary,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 4.sp
                    )
                    
                    Text(
                        text = "Empower Your Knowledge",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 2.sp
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(tween(1000, 500))
                ) {
                    Text(
                        text = "Connect with a global network of learners and challenge yourself with assessments tailored to your curriculum.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(48.dp))

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(1000, 1000))
                ) {
                    Column {
                        Button(
                            onClick = onStartJourney,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            shape = RoundedCornerShape(32.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                        ) {
                            Text(
                                "GET STARTED",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = onLogin,
                                modifier = Modifier.weight(1f).height(50.dp),
                                shape = RoundedCornerShape(32.dp),
                                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(width = 1.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary)
                            ) {
                                Text("SIGN IN")
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            OutlinedButton(
                                onClick = onSignUp,
                                modifier = Modifier.weight(1f).height(50.dp),
                                shape = RoundedCornerShape(32.dp),
                                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(width = 1.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary)
                            ) {
                                Text("JOIN NOW")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Sign in with Google Button
                        OutlinedButton(
                            onClick = {
                                val gso = com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(context.getString(R.string.default_web_client_id))
                                    .requestEmail()
                                    .build()
                                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                                googleSignInLauncher.launch(googleSignInClient.signInIntent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(32.dp),
                            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(width = 1.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
                        ) {
                            Icon(
                                painter = painterResource(id = android.R.drawable.ic_menu_compass), // Placeholder for Google Icon
                                contentDescription = "Google Sign In",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SIGN IN WITH GOOGLE", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    LxquizTheme {
        WelcomeScreen(
            onStartJourney = {},
            onLogin = {},
            onSignUp = {},
            onGoogleSignIn = {}
        )
    }
}
