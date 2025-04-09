package com.example.psdmclientapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.psdmclientapp.viewmodel.DecisionPhaseViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecisionPhaseScreen(
    navController: NavHostController,
    sessionId: Long,
    viewModel: DecisionPhaseViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state = viewModel.state
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(sessionId) {
        viewModel.loadSession(sessionId)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Decision Phase") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Odabir metoda: ${state.decisionMethod}", style = MaterialTheme.typography.titleMedium)

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.ideas) { idea ->
                    val rating = state.ratings[idea.id] ?: 3
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(idea.title)
                        Slider(
                            value = rating.toFloat(),
                            onValueChange = { newValue ->
                                viewModel.setRating(idea.id, newValue.toInt())
                            },
                            valueRange = 1f..5f,
                            steps = 3
                        )
                        Text("Ocjena: $rating")
                    }
                }
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.submitRatings()
                        navController.navigate("results/${sessionId}")
                    }
                },
                enabled = state.ratings.isNotEmpty()
            ) {
                Text("Pošalji ocjene")
            }

            state.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
