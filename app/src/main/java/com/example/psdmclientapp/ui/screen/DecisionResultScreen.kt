package com.example.psdmclientapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.psdmclientapp.viewmodel.DecisionResultViewModel

@Composable
fun DecisionResultScreen(
    navController: NavHostController,
    problemId: Long,
    sessionId: Long,
    viewModel: DecisionResultViewModel = viewModel()
) {
    val scoredSolutions = viewModel.scoredSolutions
    val winningSolution = viewModel.winningSolution

    val scrollState = rememberScrollState()


    LaunchedEffect(Unit) {
        viewModel.determineWinner()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (winningSolution != null) "🎉 Chosen Solution" else "❌ No Unique Winner",
            style = MaterialTheme.typography.headlineSmall,
            color = if (winningSolution != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (scoredSolutions.isNotEmpty()) {
            Text("Top 5 Solutions", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            scoredSolutions.forEach { scored ->
                val solution = scored.solution
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = if (solution.chosen)
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    else
                        CardDefaults.cardColors()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Title: ${solution.title}", style = MaterialTheme.typography.titleMedium)
                            Text("Score: %.2f".format(scored.score), style = MaterialTheme.typography.bodySmall)
                        }

                        if (solution.chosen) {
                            Text(
                                text = "✅ Chosen Solution",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (solution.attributes.isNotEmpty()) {
                            Text("Attributes:", style = MaterialTheme.typography.titleSmall)
                            solution.attributes.forEach {
                                Text("• ${it.title}: ${it.value}", style = MaterialTheme.typography.bodyMedium)
                            }
                        } else {
                            Text("No attributes", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

        } else {
            Text("No solutions available.")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            if (winningSolution == null) {
                Button(onClick = {
                    navController.navigate("mainMenu") {
                        popUpTo(0) { inclusive = true }
                    }
                }) {
                    Text("Start New Session")
                }
            }

            OutlinedButton(onClick = {
                navController.navigate("mainMenu") {
                    popUpTo(0) { inclusive = true }
                }
            }) {
                Text("Return Home")
            }
        }
    }
}

