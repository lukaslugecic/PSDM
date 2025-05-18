package com.example.psdmclientapp.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.psdmclientapp.state.VotingState

@Composable
fun BordaVotingUI(
    state: VotingState,
    onRate: (Long, Int) -> Unit
) {
    var orderedSolutions by remember(state.solutions) {
        mutableStateOf(state.solutions.toList())
    }

    LaunchedEffect(state.solutions) {
        val initialOrder = state.solutions
        orderedSolutions = initialOrder
        initialOrder.forEachIndexed { index, solution ->
            onRate(solution.id, index + 1)
        }
    }

    fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text("Sort solutions (best on top):", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        orderedSolutions.forEachIndexed { index, solution ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${index + 1}. ${solution.title}")

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = {
                                    if (index > 0) {
                                        val newOrder = orderedSolutions.toMutableList()
                                        newOrder.swap(index, index - 1)
                                        orderedSolutions = newOrder
                                        newOrder.forEachIndexed { i, s -> onRate(s.id, i + 1) }
                                    }
                                },
                                enabled = index > 0
                            ) {
                                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Move Up")
                            }

                            IconButton(
                                onClick = {
                                    if (index < orderedSolutions.lastIndex) {
                                        val newOrder = orderedSolutions.toMutableList()
                                        newOrder.swap(index, index + 1)
                                        orderedSolutions = newOrder
                                        newOrder.forEachIndexed { i, s -> onRate(s.id, i + 1) }
                                    }
                                },
                                enabled = index < orderedSolutions.lastIndex
                            ) {
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Move Down")
                            }
                        }
                    }

                    if (solution.attributes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Attributes:", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        solution.attributes.forEach { attr ->
                            Text(
                                text = "• ${attr.title}: ${attr.value}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "No attributes provided.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
