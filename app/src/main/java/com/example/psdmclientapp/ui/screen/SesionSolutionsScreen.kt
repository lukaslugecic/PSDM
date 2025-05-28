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
import com.example.psdmclientapp.viewmodel.SessionSolutionsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionSolutionsScreen(
    navController: NavHostController,
    sessionId: Long,
    viewModel: SessionSolutionsViewModel = hiltViewModel()
) {
    val solutions = viewModel.solutions
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Solutions for Problem: ${solutions.firstOrNull()?.problemTitle}") })
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
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                error != null -> {
                    Text(
                        text = "Error: $error",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    solutions.forEach { sol ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = sol.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Author: ${sol.firstName} ${sol.lastName}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                if (sol.attributes.isNotEmpty()) {
                                    Text(
                                        text = "Attributes:",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Column(modifier = Modifier.padding(start = 8.dp)) {
                                        sol.attributes.forEach { attr ->
                                            Text(
                                                text = "• ${attr.title}: ${attr.value}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "No attributes",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
