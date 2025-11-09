package com.example.psdmclientapp.ui.component

import androidx.compose.runtime.Composable
import com.example.psdmclientapp.viewmodel.CreateProblemViewModel
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.psdmclientapp.enum.DecisionMakingMethod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionForm(
    showTitleAndDescription: Boolean,
    viewModel: CreateProblemViewModel,
    onSubmit: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Solve a Problem") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }

            if (showTitleAndDescription) {
                OutlinedTextField(
                    value = viewModel.title,
                    onValueChange = { viewModel.title = it },
                    label = { Text("Problem Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = viewModel.description,
                    onValueChange = { viewModel.description = it },
                    label = { Text("Problem Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text("Problem Title", style = MaterialTheme.typography.labelMedium)
                Text(viewModel.title, style = MaterialTheme.typography.bodyLarge)

                Text("Problem Description", style = MaterialTheme.typography.labelMedium)
                Text(viewModel.description, style = MaterialTheme.typography.bodyLarge)
            }

            val problemSolvingMethods = viewModel.problemSolvingMethods
            val decisionMakingMethods = viewModel.decisionMakingMethods

            DropdownSelector(
                label = "Solving Method",
                options = problemSolvingMethods.map { it.title },
                selectedOption = problemSolvingMethods.find { it.id == viewModel.selectedSolvingMethodId }?.title,
                onOptionSelected = { title ->
                    viewModel.selectedSolvingMethodId =
                        problemSolvingMethods.find { it.title == title }?.id
                }
            )

            DropdownSelector(
                label = "Decision Method",
                options = decisionMakingMethods.map { it.title },
                selectedOption = decisionMakingMethods.find { it.id == viewModel.selectedDecisionMethodId }?.title,
                onOptionSelected = { title ->
                    viewModel.selectedDecisionMethodId =
                        decisionMakingMethods.find { it.title == title }?.id

                    if (viewModel.selectedDecisionMethodId == DecisionMakingMethod.WEIGHTED_AVERAGE_WINNER.id
                        && "Weight" !in viewModel.attributeTitles
                    ) {
                        viewModel.attributeTitles.add("Weight")
                    }
                }
            )

            Text("Session Duration", style = MaterialTheme.typography.titleMedium)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = viewModel.durationHours,
                    onValueChange = {
                        viewModel.durationHours = it.filter { ch -> ch.isDigit() }.take(2)
                    },
                    label = { Text("HH") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = viewModel.durationMinutes,
                    onValueChange = {
                        viewModel.durationMinutes = it.filter { ch -> ch.isDigit() }.take(2)
                    },
                    label = { Text("MM") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = viewModel.durationSeconds,
                    onValueChange = {
                        viewModel.durationSeconds = it.filter { ch -> ch.isDigit() }.take(2)
                    },
                    label = { Text("SS") },
                    modifier = Modifier.weight(1f)
                )
            }


            HorizontalDivider()

            Text("Attributes", style = MaterialTheme.typography.titleMedium)

            viewModel.attributeTitles.forEachIndexed { index, title ->

                val isWeightField =
                    title == "Weight" && viewModel.selectedDecisionMethodId == DecisionMakingMethod.WEIGHTED_AVERAGE_WINNER.id

                OutlinedTextField(
                    value = title,
                    onValueChange = { newTitle ->
                        if (!isWeightField) {
                            viewModel.attributeTitles[index] = newTitle
                        }
                    },
                    label = { Text("Attribute ${index + 1}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.attributeTitles.add("") }) {
                    Text("Add Attribute")
                }
                if (viewModel.attributeTitles.isNotEmpty()) {
                    Button(
                        onClick = {
                            if (viewModel.selectedDecisionMethodId == DecisionMakingMethod.WEIGHTED_AVERAGE_WINNER.id && viewModel.attributeTitles.last() == "Weight") {
                                return@Button
                            }
                            viewModel.attributeTitles.removeAt(viewModel.attributeTitles.lastIndex)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Remove Last")
                    }
                }
            }

            Button(onClick = { onSubmit() }) {
                Text("Submit")
            }
        }
    }
}
