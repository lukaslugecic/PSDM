package com.example.psdmclientapp.ui.screen


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.psdmclientapp.viewmodel.MyProblemsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProblemsScreen(navController: NavHostController, viewModel: MyProblemsViewModel = viewModel()) {
    val problemDetails = viewModel.problemDetails
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    Scaffold(topBar = { TopAppBar(title = { Text("My Problems") }) }) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())) {

            if (isLoading) {
                CircularProgressIndicator()
            } else if (error != null) {
                Text("Error: $error", color = Color.Red)
            } else {
                problemDetails.forEach { problemDetail ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)) {

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = problemDetail.problem.title, style = MaterialTheme.typography.titleMedium)
                            Text(text = problemDetail.problem.description, style = MaterialTheme.typography.bodyMedium)

                            Spacer(modifier = Modifier.height(8.dp))

                            if (problemDetail.solution != null) {
                                Text("Solution: ${problemDetail.solution.title}", color = Color.Green)
                            } else {
                                Button(
                                    onClick = { navController.navigate("createSession/${problemDetail.problem.id}") }
                                ) {
                                    Text("Start New Session")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
