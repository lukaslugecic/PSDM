package com.example.psdmclientapp.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.psdmclientapp.viewmodel.ProblemSessionsViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemSessionsScreen(
    navController: NavHostController,
    problemId: Long,
    viewModel: ProblemSessionsViewModel = hiltViewModel()
) {
    val sessions = viewModel.sessions
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    // Formatter for parsing and displaying
    val parser = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val displayFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sessions for Problem: ${sessions.firstOrNull()?.problemTitle}") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                }
                error != null -> {
                    Text(
                        text = "Error: $error",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    sessions.forEach { session ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {

                                Text(
                                    text = "Problem Solving Method: ${session.problemSolvingMethod}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Decision Making Method: ${session.decisionMakingMethod}",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                val startDateTime = LocalDateTime.parse(session.start, parser)
                                val endDateTime = LocalDateTime.parse(session.end, parser)
                                val startText = startDateTime.format(displayFormatter)
                                val endText = endDateTime.format(displayFormatter)

                                Text(
                                    text = "Start: $startText",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "End:   $endText",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                if(session.attributes.isNotEmpty()) {
                                    Text(
                                        text = "Attributes:",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                    ) {
                                        session.attributes.forEach { attribute ->
                                            Text(
                                                text = "• $attribute",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    Button(
                                        onClick = {
                                            navController.navigate("sessionSolutions/${session.id}")
                                        }
                                    ) {
                                        Text("Solutions")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
