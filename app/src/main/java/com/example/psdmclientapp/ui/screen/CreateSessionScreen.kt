package com.example.psdmclientapp.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.psdmclientapp.viewmodel.CreateProblemViewModel
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.psdmclientapp.ui.component.SessionForm
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlinx.serialization.encodeToString


@Composable
fun CreateSessionScreen(
    navController: NavHostController,
    viewModel: CreateProblemViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    SessionForm(
        showTitleAndDescription = false,
        viewModel = viewModel,
        onSubmit = {
            coroutineScope.launch {
                viewModel.submit { session ->
                    val json = Json.encodeToString(viewModel.attributeTitles.map{ it.trim() }.toList())
                    val encodedAttributes = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                    navController.navigate("inviteUsers/${session.problemId}/${session.id}/$encodedAttributes")
                }
            }
        }
    )
}
