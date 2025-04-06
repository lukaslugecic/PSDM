package com.example.psdmclientapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BrainstormScreen(navController: NavController) {
    var newIdea by remember { mutableStateOf("") }
    val ideas = remember { mutableStateListOf<String>() }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Brainstorming ideje",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Unos nove ideje
            OutlinedTextField(
                value = newIdea,
                onValueChange = { newIdea = it },
                label = { Text("Nova ideja") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (newIdea.isNotBlank()) {
                        ideas.add(newIdea.trim())
                        newIdea = ""
                    }
                },
                modifier = Modifier.align(Alignment.End)
                /*
                onClick = {
                    if (newIdea.isNotBlank()) {
                        coroutineScope.launch {
                            try {
                                val idea = Idea(
                                    content = newIdea.trim(),
                                    problemId = 1L // TODO: zamijeni stvarnim ID-jem problema
                                )
                                val result = RetrofitInstance.api.submitIdea(idea)
                                ideas.add(result.content)
                                newIdea = ""
                            } catch (e: Exception) {
                                // TODO: obrada greške
                                e.printStackTrace()
                            }
                        }
                    }
                },

              */
            ) {
                Text("Dodaj")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lista ideja
            Text(
                text = "Predložene ideje:",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(ideas) { idea ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = idea,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // TODO: Pošalji ideje i idi dalje
                    navController.navigate("voting_screen")
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Nastavi")
            }
        }
    }
}
