package com.example.psdmclientapp.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.psdmclientapp.state.VotingState

@Composable
fun MajorityVotingUI(
    state: VotingState,
    onRate: (Long, Int) -> Unit
) {

    LaunchedEffect(state.solutions) {
        if (state.solutions.isNotEmpty() && state.ratings.none { it.value == 1 }) {
            val default = state.solutions.first()
            onRate(default.id, 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text("Choose a solution:", style = MaterialTheme.typography.titleMedium)

        state.solutions.forEach { solution ->
            val isSelected = state.ratings[solution.id] == 1

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                // Toggle selection:
                                if (isSelected) {
                                    // Unselect this solution
                                    onRate(solution.id, 0)
                                } else {
                                    // Select this solution and unselect others
                                    state.solutions.forEach { s ->
                                        onRate(s.id, if (s.id == solution.id) 1 else 0)
                                    }
                                }
                            }
                        )
                        Text(
                            text = solution.title,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    if (solution.attributes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Attributes:",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        solution.attributes.forEach { attr ->
                            Text(
                                text = "• ${attr.title}: ${attr.value}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "No attributes provided.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
