package com.example.psdmclientapp.ui.screen

import android.webkit.CookieManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.psdmclientapp.MainActivity
import com.example.psdmclientapp.R
import com.example.psdmclientapp.auth.AuthManager
import com.example.psdmclientapp.auth.TokenStorage


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainMenuScreen(navController: NavHostController) {

    val context = LocalContext.current
    val activity = (context as? MainActivity)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_3),
            contentDescription = "App Logo",
            modifier = Modifier
                .height(400.dp)
                .padding(bottom = 16.dp)
        )
        Button(onClick = { navController.navigate("createProblem") }) {
            Text("Create Problem")
        }
        Button(onClick = { navController.navigate("myProblems") }) {
            Text("My Problems")
        }
        Button(onClick = { navController.navigate("joinSession") }) {
            Text("Join Session")
        }

        Button(onClick = {
            // clear any cookies immediately
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()

            (context as? MainActivity)?.startLogout()
        }) {
            Text("Log out")
        }

    }
}
