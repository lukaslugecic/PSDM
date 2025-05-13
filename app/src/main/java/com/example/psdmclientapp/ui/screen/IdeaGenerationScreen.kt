package com.example.psdmclientapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.psdmclientapp.network.ApiClient
import com.example.psdmclientapp.viewmodel.IdeaGenerationViewModel
import com.example.psdmclientapp.viewmodel.IdeaGenerationViewModel.NavigationCommand
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun IdeaGenerationScreen(
    navController: NavHostController,
    problemId: Long,
    sessionId: Long,
    attributeTitles: List<String>,
    viewModel: IdeaGenerationViewModel = viewModel()
) {
    rememberCoroutineScope()
    val state = viewModel.state

    var ideaInput by remember { mutableStateOf("") }

    var attributeInputs by remember {
        mutableStateOf(attributeTitles.associateWith { "" })
    }

    var showInputs by remember { mutableStateOf(true) }


    val navCommand = viewModel.navigationCommand

    LaunchedEffect(navCommand) {
        navCommand?.let {
            navController.navigate(it.route)
            viewModel.navigationCommand = null
        }
    }

    LaunchedEffect(sessionId) {
        val shouldRedirect = viewModel.loadSession(sessionId)

        if (shouldRedirect && viewModel.state.currentUserId != null) {
            val json = Json.encodeToString(attributeTitles)
            val encodedAttributes = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
            val newSessionId = ApiClient.userApi.getCurrentSubSessionId(viewModel.state.currentUserId!!)

            val route = "ideaGeneration/$problemId/$newSessionId/$encodedAttributes"
            viewModel.navigationCommand = NavigationCommand(route)
        } else {
            viewModel.maybeNavigateAfterDelay(attributeTitles, problemId, sessionId)
        }
    }


    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            viewModel.refreshSolutions()
        }
    }


    Scaffold(
        topBar = { TopAppBar(title = { Text(state.problemTitle) }) }
    ) { padding ->

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
                Text(state.problemDescription)

                HorizontalDivider()

                Text("Ideas:", style = MaterialTheme.typography.titleMedium)

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(state.solutions) { idea ->
                        val isMine = idea.userId == state.currentUserId
                        Row(
                            horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Surface(
                                color = if (isMine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = idea.title,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )

                                    idea.attributes.forEach { attr ->
                                        Text(
                                            text = "${attr.title}: ${attr.value}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
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
                        value = ideaInput,
                        onValueChange = { ideaInput = it },
                        label = { Text("Idea Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

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

                    Button(
                        onClick = {
                            viewModel.submitSolutionWithAttributes(
                                ideaInput,
                                attributeInputs.map { (title, value) -> title to value }
                            )
                            ideaInput = ""
                            attributeInputs = attributeTitles.associateWith { "" }
                        },
                        modifier = Modifier.align(Alignment.End),
                        enabled = ideaInput.isNotBlank() && attributeInputs.values.all { it.isNotBlank() }
                    ) {
                        Text("Send Idea")
                    }
                }




                state.errorMessage?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

