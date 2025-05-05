package com.example.psdmclientapp.ui.screen

import com.example.psdmclientapp.viewmodel.InviteUsersViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteUsersScreen(
    navController: NavHostController,
    problemId: Long,
    sessionId: Long,
    attributeTitles: List<String>,
    viewModel: InviteUsersViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val users = viewModel.availableUsers
    val selectedUsers = viewModel.selectedUserIds
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    LaunchedEffect(Unit) {
        viewModel.loadUsers() // pretend we're fetching from backend
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Invite Users") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
        ) {
            Text("Odaberi korisnike koje želiš pozvati:", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(users) { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(user.name + " " + user.surname)
                        Checkbox(
                            checked = selectedUsers.contains(user.id),
                            onCheckedChange = {
                                viewModel.toggleUserSelection(user.id)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.sendInvites(sessionId)
                        if (viewModel.errorMessage == null) {
                           // navController.navigate("sessionLobby/$sessionId")

                            val json = Json.encodeToString(attributeTitles)
                            val encodedAttributes = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())

                            navController.navigate("ideaGeneration/$problemId/$sessionId/$encodedAttributes")
                        }
                    }
                },
                enabled = !isLoading
            ) {
                Text("Pozovi odabrane")
            }

            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
