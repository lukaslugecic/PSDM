package com.example.psdmclientapp.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        // Kasnije dodajemo ostale rute:
        // composable("problem_input") { ProblemInputScreen(navController) }

        composable("problem_input") {
            ProblemInputScreen(navController)
        }

        composable("brainstorm_screen") {
            BrainstormScreen(navController)
        }
    }
}
