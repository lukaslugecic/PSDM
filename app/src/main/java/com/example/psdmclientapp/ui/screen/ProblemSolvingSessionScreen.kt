package com.example.psdmclientapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.psdmclientapp.viewmodel.ProblemSolvingSessionViewModel
import kotlinx.coroutines.delay

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProblemSolvingSessionScreen(
    navController: NavHostController,
    sessionId: Long,
    viewModel: ProblemSolvingSessionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state

    var ideaInput by remember { mutableStateOf("") }

    LaunchedEffect(sessionId) {
        viewModel.loadSession(sessionId)

        while (true) {
            delay(4000) // polling svakih 4 sekunde
            viewModel.refreshSolutions()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("ProblemSolving") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Problem: ${state.problemTitle}", style = MaterialTheme.typography.titleMedium)
            Text("Opis: ${state.problemDescription}")

            OutlinedTextField(
                value = ideaInput,
                onValueChange = { ideaInput = it },
                label = { Text("Unesi ideju") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.submitSolution(ideaInput)
                    ideaInput = ""
                },
                enabled = ideaInput.isNotBlank()
            ) {
                Text("Pošalji ideju")
            }

            Divider()

            Text("Ideje:", style = MaterialTheme.typography.titleMedium)

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.solutions) { idea ->
                    Text("- ${idea.title} (${idea.userId})", modifier = Modifier.padding(4.dp))
                }
            }

            if (state.isOwner) {
                Button(
                    onClick = {
                        viewModel.endProblemSolving()
                        navController.navigate("decisionPhase/${sessionId}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Završi brainstorming")
                }
            }

            state.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
