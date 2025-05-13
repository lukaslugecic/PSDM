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

            Column(modifier = Modifier.padding(8.dp)) {
                Text(solution.title)

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

