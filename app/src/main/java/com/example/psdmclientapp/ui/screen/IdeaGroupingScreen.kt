package com.example.psdmclientapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.psdmclientapp.viewmodel.IdeaGenerationViewModel

@Composable
fun IdeaGroupingScreen(
    navController: NavHostController,
    problemId: Long,
    sessionId: Long,
    viewModel: IdeaGenerationViewModel = viewModel()
) {
    val state = viewModel.state
    val selectedSolutions = remember { mutableStateListOf<Long>() }
    var newIdeaText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadSession(sessionId)
    }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Odaberi ideje za grupiranje", style = MaterialTheme.typography.titleLarge)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.solutions) { solution ->
                val isSelected = selectedSolutions.contains(solution.id)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable {
                            if (isSelected) selectedSolutions.remove(solution.id)
                            else selectedSolutions.add(solution.id)
                        }
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = {
                            if (it) selectedSolutions.add(solution.id)
                            else selectedSolutions.remove(solution.id)
                        }
                    )
                    Text(solution.title, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        OutlinedTextField(
            value = newIdeaText,
            onValueChange = { newIdeaText = it },
            label = { Text("Nova ideja (kombinacija)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.submitGroupedSolution(newIdeaText, selectedSolutions.toList())
                newIdeaText = ""
                selectedSolutions.clear()
                navController.popBackStack() // Return to previous screen
            },
            enabled = newIdeaText.isNotBlank() && selectedSolutions.isNotEmpty(),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Pošalji novu grupnu ideju")
        }
    }
}
