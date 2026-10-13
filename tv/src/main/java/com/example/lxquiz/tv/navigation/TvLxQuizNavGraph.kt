package com.example.lxquiz.tv.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lxquiz.tv.ui.TvHomeScreen
import com.example.lxquiz.ui.screens.quiz.QuizScreen

@Composable
fun TvLxQuizNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "tv_home"
    ) {
        composable("tv_home") {
            TvHomeScreen(
                onStartQuiz = { subject ->
                    navController.navigate("tv_quiz/$subject")
                },
                onOpenAiAssistant = {
                    navController.navigate("tv_home")
                }
            )
        }

        composable("tv_quiz/{subject}") {
            QuizScreen(
                onBack = { navController.popBackStack() },
                onComplete = { navController.popBackStack() },
                onNavigate = { dest ->
                    if (dest == "home") navController.popBackStack()
                }
            )
        }
    }
}
