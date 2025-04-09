package com.example.psdmclientapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.psdmclientapp.ui.screen.DecisionPhaseScreen
import com.example.psdmclientapp.ui.screen.InviteUsersScreen
import com.example.psdmclientapp.ui.screen.MainMenuScreen
import com.example.psdmclientapp.ui.screen.ProblemSolvingSessionScreen
import com.example.psdmclientapp.ui.screen.SessionLobbyScreen
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

        composable(
            route = "inviteUsers/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L
            InviteUsersScreen(navController, sessionId)
        }

        composable(
            route = "sessionLobby/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L
            SessionLobbyScreen(navController, sessionId)
        }

        composable(
            route = "session/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L
            ProblemSolvingSessionScreen(navController, sessionId)
        }

        composable(
            "decisionPhase/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L
            DecisionPhaseScreen(navController, sessionId)
        }

    }

}

