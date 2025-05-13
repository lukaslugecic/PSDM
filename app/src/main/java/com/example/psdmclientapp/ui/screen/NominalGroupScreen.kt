package com.example.psdmclientapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.psdmclientapp.model.SolutionResponse
import com.example.psdmclientapp.viewmodel.IdeaGenerationViewModel
import kotlinx.coroutines.delay

@Composable
fun DropdownMenuDemo(
    ideas: List<SolutionResponse>,
    onIdeaSelected: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedTitle by remember { mutableStateOf("Odaberi svoju ideju") }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedTitle)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            ideas.forEach { idea ->
                DropdownMenuItem(
                    text = { Text(idea.title) },
                    onClick = {
                        selectedTitle = idea.title
                        onIdeaSelected(idea.id)
                        expanded = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NominalGroupScreen(
    navController: NavHostController,
    problemId: Long,
    sessionId: Long,
    viewModel: IdeaGenerationViewModel = viewModel() // ili hiltViewModel()
) {
    val state = viewModel.state
    val coroutineScope = rememberCoroutineScope()

    var newIdeaText by remember { mutableStateOf("") }
    var selectedIdeaId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(sessionId) {
        viewModel.loadSession(sessionId)
        //viewModel.refreshTurn()

        while (true) {
            delay(4000)
            viewModel.refreshSolutions()
        //    viewModel.refreshTurn() // ako imaš metodu koja dohvaća čiji je red
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Nominal Group Technique") })
        }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(text = "Problem: ${state.problemTitle}")
                Text(text = "Opis: ${state.problemDescription}")

                HorizontalDivider()

                Text("Ideje po korisniku:", style = MaterialTheme.typography.titleMedium)

                state.solutions
                    .groupBy { it.userId }
                    .forEach { (userName, ideas) ->
                        Text("$userName", style = MaterialTheme.typography.labelLarge)
                        ideas.forEach { idea ->
                            Text("- ${idea.title}", modifier = Modifier.padding(start = 8.dp))
                        }
                    }

                HorizontalDivider()

                if (true) {
                    Text("Tvoj je red!", style = MaterialTheme.typography.titleLarge)

                    DropdownMenuDemo(
                        ideas = state.solutions.filter { it.userId == state.currentUserId },
                        onIdeaSelected = { selectedIdeaId = it }
                    )

                    OutlinedTextField(
                        value = newIdeaText,
                        onValueChange = { newIdeaText = it },
                        label = { Text("Ili unesi novu ideju") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            val ideaToSubmit = newIdeaText.ifBlank {
                                state.solutions.find { it.id == selectedIdeaId }?.title.orEmpty()
                            }
                        //    viewModel.submitTurnIdea(ideaToSubmit)
                            newIdeaText = ""
                            selectedIdeaId = null
                        },
                        enabled = newIdeaText.isNotBlank() || selectedIdeaId != null
                    ) {
                        Text("Objavi ideju")
                    }

                } else {
                    Text("Čekaj svoj red...", style = MaterialTheme.typography.bodyLarge)
                }

                state.errorMessage?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
