package com.example.psdmclientapp.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.psdmclientapp.state.VotingState

@Composable
fun AverageVotingUI(
    state: VotingState,
    onRate: (Long, Int) -> Unit
) {
    Column {
        Text("Rate each solution (1-5):", style = MaterialTheme.typography.titleMedium)

        state.solutions.forEach { solution ->
            val rating = state.ratings[solution.id] ?: 3 // default to 3

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = solution.title,
                        style = MaterialTheme.typography.titleMedium
                    )

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

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Rating: $rating", style = MaterialTheme.typography.bodyMedium)

                    Slider(
                        value = rating.toFloat(),
                        onValueChange = { newValue ->
                            onRate(solution.id, newValue.toInt().coerceIn(1, 5))
                        },
                        valueRange = 1f..5f,
                        steps = 3
                    )
                }
            }
        }
    }
}


