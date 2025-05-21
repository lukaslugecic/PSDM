package com.example.psdmclientapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.psdmclientapp.auth.AuthManager
import com.example.psdmclientapp.auth.TokenStorage
import com.example.psdmclientapp.ui.screen.InviteUsersScreen
import com.example.psdmclientapp.ui.screen.MainMenuScreen
import com.example.psdmclientapp.ui.screen.IdeaGenerationScreen
import com.example.psdmclientapp.ui.screen.SessionLobbyScreen
import com.example.psdmclientapp.ui.screen.CreateProblemScreen
import com.example.psdmclientapp.ui.screen.CreateSessionScreen
import com.example.psdmclientapp.ui.screen.DecisionResultScreen
import com.example.psdmclientapp.ui.screen.IdeaGroupingScreen
import com.example.psdmclientapp.ui.screen.LoginScreen
import com.example.psdmclientapp.ui.screen.MyProblemsScreen
import com.example.psdmclientapp.ui.screen.NominalGroupScreen
import com.example.psdmclientapp.ui.screen.VotingScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var authLauncher: ActivityResultLauncher<Intent>
    private var onLoginSuccess: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ① Register the launcher before you ever use it:
        authLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            // We don’t actually need the resultCode/data here,
            // since the browser redirect comes back via onNewIntent()
            Log.d("AUTH_MANAGER", "Returned from browser to activity result")
        }

        setContent {
            MaterialTheme {
                AppNavGraph()
            }
        }
    }

    /**
     * ② Delegate into AuthManager, passing the launcher we just initialized
     */
    fun startLogin(onSuccess: () -> Unit) {
        onLoginSuccess = onSuccess
        AuthManager.startAuthWithLauncher(this, authLauncher)
    }

    /**
     * ③ Handle the browser→app redirect here
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { uri ->
            Log.d("AUTH_MANAGER", "Redirect URI: $uri")
            AuthManager.handleAuthResponse(this, intent) { token ->
                if (token != null) {
                    TokenStorage.saveTokens(this, token, null)
                    onLoginSuccess?.invoke()
                    onLoginSuccess = null
                } else {
                    Log.e("AUTH_MANAGER", "No token in response")
                }
            }
        }
    }
}


@Composable
fun AppNavGraph(startDestination: String = "login") {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") { LoginScreen(navController) }
        composable("mainMenu") { MainMenuScreen(navController) }


        composable("createProblem") { CreateProblemScreen(navController) }

        composable(
            route = "createSession/{problemId}",
            arguments = listOf(
                navArgument("problemId") { type = NavType.LongType }
            )
        ) {
            CreateSessionScreen(navController)
        }


        composable("myProblems") { MyProblemsScreen(navController) }

        composable(
            route = "inviteUsers/{problemId}/{sessionId}/{attributes}",
            arguments = listOf(
                navArgument("problemId") { type = NavType.LongType },
                navArgument("sessionId") { type = NavType.LongType },
                navArgument("attributes") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val problemId = backStackEntry.arguments?.getLong("problemId") ?: 0L
            val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L

            val attributesEncoded = backStackEntry.arguments?.getString("attributes") ?: ""
            val jsonDecoded = URLDecoder.decode(attributesEncoded, StandardCharsets.UTF_8.toString())
            val attributeTitles: List<String> = Json.decodeFromString(jsonDecoded)

            InviteUsersScreen(navController, problemId, sessionId, attributeTitles)
        }

        composable(
            route = "sessionLobby/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L
            SessionLobbyScreen(navController, sessionId)
        }

        composable(
            route = "ideaGeneration/{problemId}/{sessionId}/{attributes}",
            arguments = listOf(
                navArgument("problemId") { type = NavType.LongType },
                navArgument("sessionId") { type = NavType.LongType },
                navArgument("attributes") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val problemId = backStackEntry.arguments?.getLong("problemId") ?: 0L
            val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L

            val attributesEncoded = backStackEntry.arguments?.getString("attributes") ?: ""
            val jsonDecoded = URLDecoder.decode(attributesEncoded, StandardCharsets.UTF_8.toString())
            val attributeTitles: List<String> = Json.decodeFromString(jsonDecoded)

            println("ENCODED FROM URL : $attributesEncoded")
           // println("FIRST: ${attributeTitles[0]}")

            IdeaGenerationScreen(navController, problemId, sessionId, attributeTitles)
        }

        composable(
            route = "grouping/{problemId}/{sessionId}/{attributes}",
            arguments = listOf(
                navArgument("problemId") { type = NavType.LongType },
                navArgument("sessionId") { type = NavType.LongType },
                navArgument("attributes") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val problemId = backStackEntry.arguments?.getLong("problemId") ?: 0L
            val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L

            val attributesEncoded = backStackEntry.arguments?.getString("attributes") ?: ""
            val jsonDecoded = URLDecoder.decode(attributesEncoded, StandardCharsets.UTF_8.toString())
            val attributeTitles: List<String> = Json.decodeFromString(jsonDecoded)

            IdeaGroupingScreen(navController, problemId, sessionId, attributeTitles)
        }

        composable(
            "nominal/{problemId}/{sessionId}/{attributes}",
            arguments = listOf(
                navArgument("problemId") { type = NavType.LongType },
                navArgument("sessionId") { type = NavType.LongType },
                navArgument("attributes") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val problemId = backStackEntry.arguments?.getLong("problemId") ?: 0L
            val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L

            val attributesEncoded = backStackEntry.arguments?.getString("attributes") ?: ""
            val jsonDecoded = URLDecoder.decode(attributesEncoded, StandardCharsets.UTF_8.toString())
            val attributeTitles: List<String> = Json.decodeFromString(jsonDecoded)

            NominalGroupScreen(navController, problemId, sessionId)
        }

        composable(
            "voting/{problemId}/{sessionId}/{attributes}",
            arguments = listOf(
                navArgument("problemId") { type = NavType.LongType },
                navArgument("sessionId") { type = NavType.LongType },
                navArgument("attributes") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val problemId = backStackEntry.arguments?.getLong("problemId") ?: 0L
            val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L

            val attributesEncoded = backStackEntry.arguments?.getString("attributes") ?: ""
            val jsonDecoded = URLDecoder.decode(attributesEncoded, StandardCharsets.UTF_8.toString())
            val attributeTitles: List<String> = Json.decodeFromString(jsonDecoded)

            VotingScreen(navController, problemId, sessionId)
        }

        composable(
            "decisionResult/{problemId}/{sessionId}",
            arguments = listOf(
                navArgument("problemId") { type = NavType.LongType },
                navArgument("sessionId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val problemId = backStackEntry.arguments?.getLong("problemId") ?: 0L
            val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L
            DecisionResultScreen(navController, problemId, sessionId)
        }

    }

}

