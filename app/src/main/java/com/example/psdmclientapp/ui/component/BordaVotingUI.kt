package com.example.psdmclientapp.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
    val solutionRatings = remember(state.ratings, state.solutions) {
        val defaultRatings = state.solutions.associate { it.id to (state.solutions.indexOf(it) + 1) }
        defaultRatings.mapValues { state.ratings[it.key] ?: it.value }
    }

    val orderedSolutions = state.solutions.sortedBy { solutionRatings[it.id] }

    fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }

    Text("Sort solutions (best on top):", style = MaterialTheme.typography.titleMedium)

    LazyColumn {
        itemsIndexed(orderedSolutions) { index, solution ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text("${index + 1}. ${solution.title}")

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = {
                                if (index > 0) {
                                    val newOrder = orderedSolutions.toMutableList()
                                    newOrder.swap(index, index - 1)
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
                                    newOrder.forEachIndexed { i, s -> onRate(s.id, i + 1) }
                                }
                            },
                            enabled = index < orderedSolutions.lastIndex
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Move Down")
                        }
                    }
                }
            }
        }
    }
}

