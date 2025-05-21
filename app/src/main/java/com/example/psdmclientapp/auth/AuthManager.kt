
package com.example.psdmclientapp.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.net.toUri
import net.openid.appauth.*
import java.net.HttpURLConnection
import java.net.URL
import net.openid.appauth.connectivity.ConnectionBuilder
import android.net.Uri


object AuthManager {

    private var pendingAuthRequest: AuthorizationRequest? = null

    const val CLIENT_ID     = "psdm-android-client"
    const val REDIRECT_URI  = "com.example.psdmclientapp://oauth/callback"
    private const val AUTH_ENDPOINT  = "http://192.168.186.220:8080/realms/psdm-realm/protocol/openid-connect/auth"
    private const val TOKEN_ENDPOINT = "http://192.168.186.220:8080/realms/psdm-realm/protocol/openid-connect/token"


    private val allowHttpConnectionBuilder = ConnectionBuilder { uri: Uri ->
        (URL(uri.toString()).openConnection() as HttpURLConnection).apply {
            // you can tweak timeouts here if you like:
            connectTimeout = 10_000
            readTimeout = 10_000
        }
    }

    // ③ Create a shared AppAuthConfiguration with your builder
    private val insecureConfig = AppAuthConfiguration.Builder()
        .setConnectionBuilder(allowHttpConnectionBuilder)
        .build()


    val serviceConfig = AuthorizationServiceConfiguration(
        AUTH_ENDPOINT.toUri(),
        TOKEN_ENDPOINT.toUri()
    )

    fun startAuthWithLauncher(
        activity: Activity,
        launcher: ActivityResultLauncher<Intent>
    ) {
        val authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            CLIENT_ID,
            ResponseTypeValues.CODE,
            REDIRECT_URI.toUri()
        ).build()
        pendingAuthRequest = authRequest

        AuthorizationService(activity, insecureConfig).let { svc ->
            launcher.launch(svc.getAuthorizationRequestIntent(authRequest))
            Log.d("AUTH_MANAGER", "Launched authorization intent")
        }
    }

    fun handleAuthResponse(
        context: Context,
        intent: Intent,
        callback: (String?) -> Unit
    ) {
        // 1) Try the AppAuth extras, 2) fall back to parsing the raw URI
        val response = AuthorizationResponse.fromIntent(intent)
            ?: intent.data
                ?.takeIf { pendingAuthRequest != null }
                ?.let { uri ->
                    AuthorizationResponse.Builder(pendingAuthRequest!!)
                        .fromUri(uri)
                        .build()
                }
        val exception = AuthorizationException.fromIntent(intent)

        if (response == null) {
            Log.e("AUTH_MANAGER", "Auth failed or invalid response", exception)
            callback(null)
            return
        }

        Log.d("AUTH_MANAGER", "Authorization response received: $response")
        Log.d("AUTH_MANAGER", "Authorization response id token received: ${response.idToken}")
        pendingAuthRequest = null

        // process:com.example.psdmclientapp tag:TOKEN_STORAGE tag:AUTH_MANAGER tag:AUTH_LAUNCHER tag:HTTP_TEST
        val tokenRequest = response.createTokenExchangeRequest()

        val authService = AuthorizationService(context, insecureConfig)
        try {
            authService.performTokenRequest(tokenRequest) { tokenResponse, ex ->
                if (ex != null) {
                    Log.e("AUTH_MANAGER", "Token exchange FAILED: error=${ex.error} desc=${ex.errorDescription}")
                    callback(null)
                } else {
                    Log.d("AUTH_MANAGER", "Token exchange HTTP response: $tokenResponse")
                    val accessToken = tokenResponse?.accessToken
                    val refreshToken = tokenResponse?.refreshToken

                    if (!accessToken.isNullOrEmpty()) {
                        Log.d("AUTH_MANAGER", "Access token received: $accessToken")
                        TokenStorage.saveTokens(
                            context,
                            tokenResponse.accessToken!!,
                            tokenResponse.refreshToken
                        )
                        callback(accessToken)
                    } else {
                        Log.e("AUTH_MANAGER", "Access token was null or empty")
                        callback(null)
                    }
                }
                authService.dispose()
            }
        } catch (e: Exception) {
            Log.e("AUTH_MANAGER", "performTokenRequest threw", e)
            callback(null)
        }
    }
}




