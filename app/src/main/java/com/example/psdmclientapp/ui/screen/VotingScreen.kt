package com.example.psdmclientapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
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
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
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
                        AverageVotingUI(
                            state = state,
                            onRate = { id, rating -> viewModel.setRating(id, rating) }
                        )
                    }
                }
            }
        }
    }
}




