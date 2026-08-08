package com.example.lxquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.lxquiz.core.designsystem.theme.LxquizTheme
import com.example.lxquiz.navigation.LxQuizNavGraph
import com.example.lxquiz.core.security.SecurityManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var securityManager: SecurityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LxquizTheme {
                val navController = rememberNavController()
                LxQuizNavGraph(navController = navController)
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus) {
            securityManager.notifyFocusLoss()
        }
    }
}
