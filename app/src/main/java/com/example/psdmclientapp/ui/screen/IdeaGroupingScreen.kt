package com.example.psdmclientapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.psdmclientapp.viewmodel.IdeaGroupingViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeaGroupingScreen(
    navController: NavHostController,
    problemId: Long,
    sessionId: Long,
    attributeTitles: List<String>,
    viewModel: IdeaGroupingViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val selectedSolutions = remember { mutableStateListOf<Long>() }

    var newIdeaText by remember { mutableStateOf("") }

    var attributeInputs by remember {
        mutableStateOf(attributeTitles.associateWith { "" })
    }

    var showInputs by remember { mutableStateOf(true) }

    val navCommand = viewModel.navigationCommand

    LaunchedEffect(sessionId) {
        viewModel.loadSession(sessionId)
        viewModel.navigateAfterDelay(attributeTitles, problemId, sessionId)
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            viewModel.refreshSolutions()
        }
    }

    LaunchedEffect(navCommand) {
        navCommand?.let {
            navController.navigate(it.route)
            viewModel.navigationCommand = null
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Idea Clarification") }) }
    ) {
        padding ->

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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
                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                Text(text = solution.title, style = MaterialTheme.typography.titleMedium)

                                solution.attributes.forEach { attr ->
                                    Text(
                                        text = "${attr.title}: ${attr.value}",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }

                        }
                    }
                }


                HorizontalDivider()

                Button(
                    onClick = { showInputs = !showInputs },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text(if (showInputs) "Hide Entry" else "Show Entry")
                }

                if(showInputs) {
                    OutlinedTextField(
                        value = newIdeaText,
                        onValueChange = { newIdeaText = it },
                        label = { Text("New Idea") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if(attributeTitles.isNotEmpty()) {
                        Text("Attributes:", style = MaterialTheme.typography.labelLarge)

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            attributeTitles.forEach { title ->
                                OutlinedTextField(
                                    value = attributeInputs[title] ?: "",
                                    onValueChange = { newValue ->
                                        attributeInputs = attributeInputs.toMutableMap().also {
                                            it[title] = newValue
                                        }
                                    },
                                    label = { Text(title) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.submitGroupedSolution(
                                newIdeaText,
                                selectedSolutions.toList(),
                                attributeInputs.map { (title, value) -> title to value }
                            )
                            newIdeaText = ""
                            selectedSolutions.clear()
                            attributeInputs = attributeTitles.associateWith { "" }
                        },
                        enabled = newIdeaText.isNotBlank() && selectedSolutions.isNotEmpty() && attributeInputs.values.all { it.isNotBlank() },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Send New Refined Idea")
                    }
                }
            }
        }
    }
}
