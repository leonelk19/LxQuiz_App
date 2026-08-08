package com.example.lxquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lxquiz.ui.screens.*
import com.example.lxquiz.ui.theme.LxquizTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LxquizTheme {
                LxQuizApp()
            }
        }
    }
}

@Composable
fun LxQuizApp() {
    val navController = rememberNavController()
    
    val onNavigate: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo("home") { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(
                onStartJourney = { navController.navigate("home") },
                onLogin = { navController.navigate("login") },
                onSignUp = { navController.navigate("signup") }
            )
        }
        composable("login") {
            LoginScreen(
                onBack = { navController.popBackStack() },
                onSignIn = { navController.navigate("home") },
                onCreateAccount = { navController.navigate("signup") }
            )
        }
        composable("signup") {
            SignUpScreen(
                onBack = { navController.popBackStack() },
                onSignUp = { navController.navigate("home") },
                onLogin = { navController.navigate("login") }
            )
        }
        composable("home") {
            HomeScreen(
                onQuizSelected = { navController.navigate("field_selection") },
                onNavigate = onNavigate
            )
        }
        composable("field_selection") {
            FieldOfStudyScreen(
                onBack = { navController.popBackStack() },
                onContinueToSubjects = { field -> 
                    navController.navigate("subject_selection/$field")
                }
            )
        }
        composable(
            route = "subject_selection/{field}",
            arguments = listOf(navArgument("field") { type = NavType.StringType })
        ) { backStackEntry ->
            val field = backStackEntry.arguments?.getString("field") ?: ""
            SubjectSelectionScreen(
                field = field,
                onBack = { navController.popBackStack() },
                onContinueToQuiz = { subject ->
                    navController.navigate("loading/$subject")
                }
            )
        }
        composable(
            route = "loading/{subject}",
            arguments = listOf(navArgument("subject") { type = NavType.StringType })
        ) { backStackEntry ->
            val subject = backStackEntry.arguments?.getString("subject") ?: ""
            LoadingScreen(
                subject = subject,
                onFinished = {
                    navController.navigate("quiz") {
                        popUpTo("field_selection") { inclusive = true }
                    }
                }
            )
        }
        composable("quiz") {
            QuizScreen(
                onBack = { navController.popBackStack() },
                onComplete = { navController.navigate("result") },
                onNavigate = onNavigate
            )
        }
        composable("result") {
            ResultScreen(
                onHome = { navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                } },
                onReview = { navController.navigate("review") },
                onNavigate = onNavigate
            )
        }
        composable("review") {
            ReviewAnswersScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("analytics") {
            AnalyticsScreen(onNavigate = onNavigate)
        }
        composable("challenge") {
            ChallengeScreen(
                onNavigate = onNavigate,
                onGenerateLink = { navController.navigate("battle_instructions") }
            )
        }
        composable("battle_instructions") {
            BattleInstructionsScreen(
                onBack = { navController.popBackStack() },
                onEnterRoom = { navController.navigate("battle_room") }
            )
        }
        composable("battle_room") {
            BattleRoomScreen(
                onBack = { navController.popBackStack() },
                onBattleStart = { navController.navigate("quiz") }
            )
        }
        composable("leaderboard") {
            LeaderboardScreen(onNavigate = onNavigate)
        }
        composable("profile") {
            ProfileScreen(onNavigate = onNavigate)
        }
    }
}
