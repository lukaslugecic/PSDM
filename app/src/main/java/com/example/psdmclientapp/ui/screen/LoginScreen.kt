package com.example.psdmclientapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.psdmclientapp.MainActivity
import com.example.psdmclientapp.auth.TokenStorage

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val token = TokenStorage.getAccessToken(context)
    if (token != null) {
        LaunchedEffect(Unit) {
            navController.navigate("mainMenu") {
                popUpTo("login") { inclusive = true }
            }
        }
        return
    }
    val activity = context as? MainActivity

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Please login to continue")
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            activity?.startLogin {
                navController.navigate("mainMenu") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }) {
            Text("Login with Keycloak")
        }
    }
}