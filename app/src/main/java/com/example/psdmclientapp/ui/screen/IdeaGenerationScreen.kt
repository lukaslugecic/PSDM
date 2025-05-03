package com.example.psdmclientapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.psdmclientapp.network.ApiClient
import com.example.psdmclientapp.viewmodel.IdeaGenerationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun IdeaGenerationScreen(
    navController: NavHostController,
    problemId: Long,
    sessionId: Long,
    viewModel: IdeaGenerationViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state

    var ideaInput by remember { mutableStateOf("") }

    LaunchedEffect(sessionId) {
        viewModel.loadSession(sessionId)
    }

    LaunchedEffect(viewModel.rotationDurationInSeconds) {
        viewModel.rotationDurationInSeconds?.let { durationInSeconds ->
            coroutineScope.launch {
                delay(durationInSeconds * 1000 + 1000)
                if (state.currentUserId != null) {
                    val newSessionId = ApiClient.userApi.getCurrentSubSessionId(state.currentUserId)
                    navController.navigate("ideaGeneration/$problemId/$newSessionId")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            viewModel.refreshSolutions()
        }
    }


    Scaffold(
        topBar = { TopAppBar(title = { Text(state.problemTitle) }) }
    ) { padding ->

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 10.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(state.problemDescription)

                HorizontalDivider()

                Text("Ideje:", style = MaterialTheme.typography.titleMedium)

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(state.solutions) { idea ->
                        val isMine = idea.userId == state.currentUserId
                        Row(
                            horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Surface(
                                color = if (isMine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Text(
                                    text = idea.title,
                                    modifier = Modifier.padding(8.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }

                HorizontalDivider()

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

                Button(
                    onClick = {
                        navController.navigate("groupIdeas/${problemId}/${sessionId}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text("Grupiraj ideje")
                }

                Button(
                    onClick = {
                        navController.navigate("nominalGroup/${problemId}/${sessionId}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text("Nominalna grupa")
                }

                Button(
                    onClick = {
                        navController.navigate("voting/${problemId}/${sessionId}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text("Glasanje")
                }


                state.errorMessage?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

