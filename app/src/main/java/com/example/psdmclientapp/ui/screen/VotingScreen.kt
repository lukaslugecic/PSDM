package com.example.psdmclientapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.psdmclientapp.enum.DecisionMakingMethod
import com.example.psdmclientapp.ui.component.AverageVotingUI
import com.example.psdmclientapp.ui.component.BordaVotingUI
import com.example.psdmclientapp.ui.component.MajorityVotingUI
import com.example.psdmclientapp.viewmodel.VotingViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun VotingScreen(
    navController: NavHostController,
    problemId: Long,
    sessionId: Long,
    viewModel: VotingViewModel = viewModel()
) {
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (state.decisionMethod) {
            DecisionMakingMethod.MAJORITY_RULE -> {
                MajorityVotingUI(
                    state = state,
                    onRate = { id, rating -> viewModel.setRating(id, rating) }
                )
            }

            DecisionMakingMethod.AVERAGE_WINNER -> {
                AverageVotingUI(
                    state = state,
                    onRate = { id, rating -> viewModel.setRating(id, rating) }
                )
            }

            DecisionMakingMethod.BORDA_RANKING -> {
                BordaVotingUI(
                    state = state,
                    onRate = { id, rating -> viewModel.setRating(id, rating) }
                )
            }

            DecisionMakingMethod.WEIGHTED_AVERAGE_WINNER -> {
                MajorityVotingUI(
                    state = state,
                    onRate = { id, rating -> viewModel.setRating(id, rating) }
                )
            }
        }

        Button(
            onClick = {
                viewModel.submitVotes {
                    navController.navigate("decisionResult/$problemId/$sessionId")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Vote")
        }

        state.errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}





