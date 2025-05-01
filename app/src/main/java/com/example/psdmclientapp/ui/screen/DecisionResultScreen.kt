package com.example.psdmclientapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.psdmclientapp.viewmodel.VotingViewModel

@Composable
fun DecisionResultScreen(
    navController: NavHostController,
    problemId: Long,
    sessionId: Long,
    viewModel: VotingViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val winningSolution = viewModel.winningSolution

        if (winningSolution != null) {
            Text(
                text = "🎉 Selected Solution",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Title: ${winningSolution.title}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = winningSolution.description)
                }
            }
        } else {
            Text(
                text = "❌ No consensus reached!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "You can try again by starting a new session.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            if (winningSolution == null) {
                Button(
                    onClick = { viewModel.startNewSession() }
                ){
                    Text("Start New Session")
                }
            }

            OutlinedButton(
                onClick = { viewModel.returnHome() }
            ) {
                Text("Return Home")
            }
        }
    }
}
