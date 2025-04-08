package com.example.psdmclientapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.psdmclientapp.ui.screen.MainMenuScreen
import com.example.psdmclientapp.ui.screen.SolveProblemScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavGraph()
            }
        }
    }
}

@Composable
fun AppNavGraph(startDestination: String = "mainMenu") {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable("mainMenu") { MainMenuScreen(navController) }
        composable("solveProblem") { SolveProblemScreen(navController) }
        composable("myProblems") { /* MyProblemsScreen(navController) */ }
        composable("joinSession") { /* JoinSessionScreen(navController) */ }
    }
}

