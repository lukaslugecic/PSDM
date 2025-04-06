package com.example.psdmclientapp.ui.screen


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {
    Scaffold { padding ->  // OVAKO: dodaj padding kao parametar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding) // OVDJE ga koristi
                .padding(16.dp), // dodatni padding ako želiš
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Dobrodošli!",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    // TODO: Login akcija
                    navController.navigate("problem_input")
                }) {
                    Text("Prijavi se")
                }
            }
        }
    }
}

