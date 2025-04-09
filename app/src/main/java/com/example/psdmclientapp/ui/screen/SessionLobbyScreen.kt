package com.example.psdmclientapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.psdmclientapp.viewmodel.SessionLobbyViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SessionLobbyScreen(
    navController: NavHostController,
    sessionId: Long,
    viewModel: SessionLobbyViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val session = viewModel.sessionInfo
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(sessionId) {
        viewModel.loadSession(sessionId)

        // Pollanje svakih 5 sekundi
        while (true) {
            delay(5000)
            viewModel.refreshSession()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Session Lobby") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize()
        ) {
            session?.let {
                Text("Problem: ${it.problemTitle}", style = MaterialTheme.typography.titleMedium)
                Text("Metoda rješavanja: ${it.solvingMethod}")
                Text("Metoda odlučivanja: ${it.decisionMethod}")

                Spacer(modifier = Modifier.height(16.dp))
                Text("Sudionici:", style = MaterialTheme.typography.titleMedium)

                LazyColumn {
                    items(it.participants) { user ->
                        Text(
                            text = "${user.name} ${if (user.accepted) "✅" else "⌛"}",
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (it.isOwner) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.startSession(sessionId)
                               // navController.navigate("session/${sessionId}")
                                navController.navigate("session/1")
                            }
                        },
                        enabled = !isLoading
                    ) {
                        Text("Pokreni sesiju")
                    }
                } else {
                    Text("Čekamo da kreator pokrene sesiju...")
                }
            }

            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
