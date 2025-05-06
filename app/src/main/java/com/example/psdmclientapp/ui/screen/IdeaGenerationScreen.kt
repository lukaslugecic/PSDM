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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state

    var ideaInput by remember { mutableStateOf("") }

    var attributeInputs by remember {
        mutableStateOf(attributeTitles.associateWith { "" })
    }

    var showInputs by remember { mutableStateOf(true) }

    LaunchedEffect(sessionId) {
        viewModel.loadSession(sessionId)
    }

    LaunchedEffect(viewModel.shouldRedirectToSubsession) {
        if (viewModel.shouldRedirectToSubsession && state.currentUserId != null) {
            val json = Json.encodeToString(attributeTitles)
            val encodedAttributes = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())

            val newSessionId = ApiClient.userApi.getCurrentSubSessionId(state.currentUserId)

            navController.navigate("ideaGeneration/$problemId/$newSessionId/$encodedAttributes") {
                popUpTo("ideaGeneration/$problemId/$sessionId/$encodedAttributes") { inclusive = true }
            }
        }
    }


    LaunchedEffect(viewModel.rotationDurationInSeconds) {
        viewModel.rotationDurationInSeconds?.let { durationInSeconds ->
            coroutineScope.launch {
                delay(durationInSeconds * 1000)
                if (state.currentUserId != null) {
                    val json = Json.encodeToString(attributeTitles)
                    val encodedAttributes = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())

                    if(viewModel.state.isSubSession) {
                        val newSessionId = runCatching {
                            ApiClient.userApi.getCurrentSubSessionId(state.currentUserId)
                        }.getOrNull()

                        if(newSessionId != null) {
                            navController.navigate("ideaGeneration/$problemId/$newSessionId/$encodedAttributes")
                        } else {
                            val parentSessionId = runCatching {
                                ApiClient.userApi.getCurrentParentSessionId(state.currentUserId)
                            }.getOrNull()
                            navController.navigate(viewModel.nextPage + "/$problemId/$parentSessionId/$encodedAttributes")
                        }
                    }

                    navController.navigate(viewModel.nextPage + "/$problemId/$sessionId/$encodedAttributes")
                }
            }
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

                Text("Ideje:", style = MaterialTheme.typography.titleMedium)

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
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(if (showInputs) "Sakrij unos" else "Dodaj ideju")
                }
                if(showInputs) {
                    OutlinedTextField(
                        value = ideaInput,
                        onValueChange = { ideaInput = it },
                        label = { Text("Naslov ideje") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Atributi:", style = MaterialTheme.typography.labelLarge)

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
                        enabled = ideaInput.isNotBlank()
                    ) {
                        Text("Pošalji ideju")
                    }
                }




                state.errorMessage?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

