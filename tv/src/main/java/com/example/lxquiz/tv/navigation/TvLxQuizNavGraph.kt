package com.example.lxquiz.tv.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.lxquiz.navigation.LxQuizNavGraph

@Composable
fun TvLxQuizNavGraph(navController: NavHostController) {
    LxQuizNavGraph(navController = navController)
}
