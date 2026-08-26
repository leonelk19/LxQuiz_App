package com.example.lxquiz.navigation

import com.example.lxquiz.ui.screens.LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.lxquiz.data.models.EducationLevel
import com.example.lxquiz.ui.screens.AnalyticsScreen
import com.example.lxquiz.ui.screens.ArenaInstructionsScreen
import com.example.lxquiz.ui.screens.ArenaRoomScreen
import com.example.lxquiz.ui.screens.AssistantScreen
import com.example.lxquiz.ui.screens.ChallengeScreen
import com.example.lxquiz.ui.screens.CompetitionScreen
import com.example.lxquiz.ui.screens.CurriculumSelectionScreen
import com.example.lxquiz.ui.screens.FieldOfStudyScreen
import com.example.lxquiz.ui.screens.HomeScreen
import com.example.lxquiz.ui.screens.LeaderboardScreen
import com.example.lxquiz.ui.screens.LoadingScreen
import com.example.lxquiz.ui.screens.ProfileScreen
import com.example.lxquiz.ui.screens.QuizScreen
import com.example.lxquiz.ui.screens.ResultScreen
import com.example.lxquiz.ui.screens.ReviewAnswersScreen
import com.example.lxquiz.ui.screens.SignUpScreen
import com.example.lxquiz.ui.screens.SubjectSelectionScreen
import com.example.lxquiz.ui.screens.WelcomeScreen
import com.example.lxquiz.ui.viewmodels.AnalyticsViewModel
import com.example.lxquiz.ui.viewmodels.ArenaViewModel
import com.example.lxquiz.ui.viewmodels.AssistantViewModel
import com.example.lxquiz.ui.viewmodels.AuthState
import com.example.lxquiz.ui.viewmodels.AuthViewModel
import com.example.lxquiz.ui.viewmodels.CompetitionViewModel
import com.example.lxquiz.ui.viewmodels.HomeViewModel
import com.example.lxquiz.ui.viewmodels.ProfileViewModel
import com.example.lxquiz.ui.viewmodels.QuizViewModel

@Composable
fun LxQuizNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    val onNavigate: (Destination) -> Unit = { destination ->
        navController.navigate(destination) {
            popUpTo<Destination.Home> { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController, 
        startDestination = Destination.Welcome
    ) {
        composable<Destination.Welcome> {
            if (authState is AuthState.Success) {
                LaunchedEffect(Unit) {
                    navController.navigate(Destination.Home) {
                        popUpTo(Destination.Welcome) { inclusive = true }
                    }
                    authViewModel.resetState()
                }
            }
            
            WelcomeScreen(
                onStartJourney = { navController.navigate(Destination.Home) },
                onLogin = { navController.navigate(Destination.Login) },
                onSignUp = { navController.navigate(Destination.SignUp) },
                onGoogleSignIn = { idToken -> authViewModel.signInWithGoogle(idToken) }
            )
        }
        
        composable<Destination.Login> {
            if (authState is AuthState.Success) {
                LaunchedEffect(Unit) {
                    navController.navigate(Destination.Home) {
                        popUpTo(Destination.Welcome) { inclusive = true }
                    }
                    authViewModel.resetState()
                }
            }

            LoginScreen(
                onBack = { 
                    authViewModel.resetState()
                    navController.popBackStack() 
                },
                onSignIn = { email, password -> authViewModel.signIn(email, password) },
                onGoogleSignIn = { idToken: String -> authViewModel.signInWithGoogle(idToken) },
                onCreateAccount = { 
                    authViewModel.resetState()
                    navController.navigate(Destination.SignUp) 
                },
                error = (authState as? AuthState.Error)?.message,
                isLoading = authState is AuthState.Loading
            )
        }
        
        composable<Destination.SignUp> {
            SignUpScreen(
                onBack = { 
                    authViewModel.resetState()
                    navController.popBackStack() 
                },
                onSignUp = { fullName, email, country, level, password ->
                    if (authViewModel.isEmailRegistered(email)) {
                        authViewModel.setError("This email is already registered. Please sign in instead.")
                    } else {
                        authViewModel.resetState()
                        authViewModel.tempFullName = fullName
                        authViewModel.tempEmail = email
                        authViewModel.tempCountry = country
                        authViewModel.tempLevel = level
                        authViewModel.tempPassword = password
                        
                        navController.navigate(
                            Destination.CurriculumSelection(
                                countryId = country?.id,
                                level = level?.name
                            )
                        )
                    }
                },
                onLogin = { 
                    authViewModel.resetState()
                    navController.navigate(Destination.Login) 
                },
                error = (authState as? AuthState.Error)?.message
            )
        }
        
        composable<Destination.CurriculumSelection> { backStackEntry ->
            val args = backStackEntry.toRoute<Destination.CurriculumSelection>()
            val level = args.level?.let { EducationLevel.valueOf(it) }

            if (authState is AuthState.Success) {
                LaunchedEffect(Unit) {
                    navController.navigate(Destination.Home) {
                        popUpTo(Destination.Welcome) { inclusive = true }
                    }
                    authViewModel.resetState()
                }
            }

            CurriculumSelectionScreen(
                initialCountryId = args.countryId,
                initialLevel = level,
                isLoading = authState is AuthState.Loading,
                onComplete = { country, level, subLevel, section, subjects ->
                    authViewModel.signUp(subLevel, section, subjects)
                }
            )
        }
        
        composable<Destination.Home> {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                onQuizSelected = { navController.navigate(Destination.FieldSelection) },
                onNavigate = { route -> 
                    navigateToDestination(navController, route)
                },
                viewModel = homeViewModel
            )
        }
        
        composable<Destination.FieldSelection> {
            FieldOfStudyScreen(
                onBack = { navController.popBackStack() },
                onContinueToSubjects = { field -> 
                    navController.navigate(Destination.SubjectSelection(field))
                },
                authViewModel = authViewModel
            )
        }
        
        composable<Destination.SubjectSelection> { backStackEntry ->
            val args = backStackEntry.toRoute<Destination.SubjectSelection>()
            SubjectSelectionScreen(
                field = args.field,
                onBack = { navController.popBackStack() },
                onContinueToQuiz = { subject ->
                    navController.navigate(Destination.Loading(subject))
                }
            )
        }
        
        composable<Destination.Loading> { backStackEntry ->
            val args = backStackEntry.toRoute<Destination.Loading>()
            val quizViewModel: QuizViewModel = hiltViewModel()
            LoadingScreen(
                subject = args.subject,
                viewModel = quizViewModel,
                onFinished = {
                    navController.navigate(Destination.Quiz) {
                        popUpTo<Destination.FieldSelection> { inclusive = true }
                    }
                }
            )
        }
        
        composable<Destination.Quiz> {
            val quizViewModel: QuizViewModel = hiltViewModel()
            QuizScreen(
                onBack = { navController.popBackStack() },
                onComplete = { navController.navigate(Destination.Result) },
                onNavigate = { route -> navigateToDestination(navController, route) },
                viewModel = quizViewModel
            )
        }
        
        composable<Destination.Result> {
            ResultScreen(
                onHome = { 
                    navController.navigate(Destination.Home) {
                        popUpTo<Destination.Home> { inclusive = true }
                    } 
                },
                onReview = { navController.navigate(Destination.Review) },
                onNavigate = { route -> navigateToDestination(navController, route) }
            )
        }
        
        composable<Destination.Review> {
            ReviewAnswersScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        composable<Destination.Analytics> {
            val analyticsViewModel: AnalyticsViewModel = hiltViewModel()
            AnalyticsScreen(
                onNavigate = { route -> navigateToDestination(navController, route) },
                viewModel = analyticsViewModel
            )
        }
        
        composable<Destination.Challenge> {
            val arenaViewModel: ArenaViewModel = hiltViewModel()
            ChallengeScreen(
                onNavigate = { route -> navigateToDestination(navController, route) },
                onGenerateLink = { navController.navigate(Destination.BattleInstructions) },
                viewModel = arenaViewModel,
                authViewModel = authViewModel
            )
        }

        composable<Destination.Competition> {
            val competitionViewModel: CompetitionViewModel = hiltViewModel()
            CompetitionScreen(
                onNavigate = { route -> navigateToDestination(navController, route) },
                viewModel = competitionViewModel
            )
        }
        
        composable<Destination.BattleInstructions> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry<Destination.Challenge>()
            }
            val arenaViewModel: ArenaViewModel = hiltViewModel(parentEntry)
            
            ArenaInstructionsScreen(
                onBack = { navController.popBackStack() },
                onEnterRoom = { navController.navigate(Destination.BattleRoom) }
            )
        }
        
        composable<Destination.BattleRoom> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry<Destination.Challenge>()
            }
            val arenaViewModel: ArenaViewModel = hiltViewModel(parentEntry)
            val uiState by arenaViewModel.uiState.collectAsState()
            
            ArenaRoomScreen(
                roomId = uiState.currentRoom?.roomId,
                onBack = { navController.popBackStack() },
                onArenaStart = { navController.navigate(Destination.Quiz) },
                viewModel = arenaViewModel
            )
        }
        
        composable<Destination.Leaderboard> {
            val competitionViewModel: CompetitionViewModel = hiltViewModel()
            LeaderboardScreen(
                onNavigate = { route -> navigateToDestination(navController, route) },
                authViewModel = authViewModel,
                competitionViewModel = competitionViewModel
            )
        }
        
        composable<Destination.Profile> {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                onNavigate = { route -> navigateToDestination(navController, route) },
                onLogout = { 
                    navController.navigate(Destination.Welcome) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewModel = profileViewModel
            )
        }
        
        composable<Destination.Assistant> {
            val assistantViewModel: AssistantViewModel = hiltViewModel()
            AssistantScreen(
                onBack = { navController.popBackStack() },
                viewModel = assistantViewModel
            )
        }
    }
}

// Helper to bridge string routes to type-safe destinations
fun navigateToDestination(navController: NavHostController, route: String) {
    val destination = when (route) {
        "home" -> Destination.Home
        "analytics" -> Destination.Analytics
        "challenge" -> Destination.Challenge
        "competition" -> Destination.Competition
        "leaderboard" -> Destination.Leaderboard
        "profile" -> Destination.Profile
        "assistant" -> Destination.Assistant
        else -> null
    }
    destination?.let {
        navController.navigate(it) {
            popUpTo<Destination.Home> { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
}
