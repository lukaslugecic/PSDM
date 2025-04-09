package com.example.psdmclientapp.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.psdmclientapp.viewmodel.SolveProblemViewModel
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolveProblemScreen(navController: NavHostController, viewModel: SolveProblemViewModel = viewModel()) {
    val coroutineScope = rememberCoroutineScope()

    val solvingMethods = listOf("Brainstorming", "NGT", "Speedstorming", "Brainwriting")
    val decisionMethods = listOf("Average Winner", "Borda Ranking", "Weighted Average")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Solve a Problem") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { viewModel.title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { viewModel.description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Solving Method", style = MaterialTheme.typography.labelSmall)
            solvingMethods.forEach { method ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = viewModel.selectedSolvingMethod == method,
                        onClick = { viewModel.selectedSolvingMethod = method }
                    )
                    Text(method)
                }
            }

            Text("Decision Method", style = MaterialTheme.typography.labelSmall)
            decisionMethods.forEach { method ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = viewModel.selectedDecisionMethod == method,
                        onClick = { viewModel.selectedDecisionMethod = method }
                    )
                    Text(method)
                }
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.submit { session ->
                            // Možeš prebaciti korisnika na ekran za pozivanje drugih
                           // navController.navigate("inviteUsers/${session.id}")
                            navController.navigate("inviteUsers/1")
                        }
                    }
                },
                enabled = !viewModel.isLoading
            ) {
                Text("Start Session")
            }

            viewModel.errorMessage?.let {
                Text(it, color = Color.Red)
            }
        }
    }
}
