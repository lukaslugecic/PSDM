package com.example.psdmclientapp.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.psdmclientapp.viewmodel.CreateProblemViewModel
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlinx.serialization.encodeToString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedText = selectedOption ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProblemScreen(navController: NavHostController, viewModel: CreateProblemViewModel = viewModel()) {
    val coroutineScope = rememberCoroutineScope()

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
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { viewModel.title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { viewModel.description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            val problemSolvingMethods = viewModel.problemSolvingMethods
            val decisionMakingMethods = viewModel.decisionMakingMethods

            DropdownSelector(
                label = "Solving Method",
                options = problemSolvingMethods.map { it.title },
                selectedOption = problemSolvingMethods.find { it.id == viewModel.selectedSolvingMethodId }?.title,
                onOptionSelected = { title ->
                    viewModel.selectedSolvingMethodId = problemSolvingMethods.find { it.title == title }?.id
                }
            )

            DropdownSelector(
                label = "Decision Method",
                options = decisionMakingMethods.map { it.title },
                selectedOption = decisionMakingMethods.find { it.id == viewModel.selectedDecisionMethodId }?.title,
                onOptionSelected = { title ->
                    viewModel.selectedDecisionMethodId = decisionMakingMethods.find { it.title == title }?.id
                }
            )

            HorizontalDivider()

            Text("Attributes", style = MaterialTheme.typography.titleMedium)

            viewModel.attributeTitles.forEachIndexed { index, title ->
                OutlinedTextField(
                    value = title,
                    onValueChange = { newTitle ->
                        viewModel.attributeTitles[index] = newTitle
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
                        onClick = { viewModel.attributeTitles.removeAt(viewModel.attributeTitles.lastIndex) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Remove Last")
                    }
                }
            }


            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.submit { session ->
                            val json = Json.encodeToString(viewModel.attributeTitles.toList())
                            val encodedAttributes = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                            navController.navigate("inviteUsers/${session.problemId}/${session.id}/$encodedAttributes")
                        }
                    }
                },
                enabled = !viewModel.isLoading
            ) {
                Text("Start Session")
            }

            viewModel.errorMessage?.let {
                Text(it, color = Color.Red)
            }
        }
    }
}
