package com.example.lxquiz.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.lxquiz.core.designsystem.theme.LxquizTheme
import com.example.lxquiz.tv.navigation.TvLxQuizNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LxquizTheme {
                val navController = rememberNavController()
                TvLxQuizNavGraph(navController = navController)
            }
        }
    }
}
