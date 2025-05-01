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
    Column {
        Text("Odaberi rješenje:", style = MaterialTheme.typography.titleMedium)

        state.solutions.forEach { solution ->
            val isSelected = state.ratings[solution.id] == 1

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = { onRate(solution.id, 1) }
                )
                Text(solution.title)
            }
        }
    }
}
