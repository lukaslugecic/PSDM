
package com.example.psdmclientapp.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.core.net.toUri
import net.openid.appauth.*

object AuthManager {

    private const val CLIENT_ID = "psdm-android-client"
    private const val REDIRECT_URI = "com.example.psdmclientapp://callback"
    private const val AUTH_ENDPOINT = "http://192.168.186.220:8080/realms/psdm-realm/protocol/openid-connect/auth"
    private const val TOKEN_ENDPOINT = "http://192.168.186.220:8080/realms/psdm-realm/protocol/openid-connect/token"

    private val serviceConfig = AuthorizationServiceConfiguration(
        AUTH_ENDPOINT.toUri(),
        TOKEN_ENDPOINT.toUri()
    )

    fun startAuth(activity: Activity, requestCode: Int) {
        val authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            CLIENT_ID,
            ResponseTypeValues.CODE,
            REDIRECT_URI.toUri()
        ).build()

        val authService = AuthorizationService(activity)
        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        activity.startActivityForResult(authIntent, requestCode)
        authService.dispose() // Dispose right after if no reuse is needed
    }

    fun handleAuthResponse(
        context: Context,
        intent: Intent,
        callback: (String?) -> Unit
    ) {
        val response = AuthorizationResponse.fromIntent(intent)
        val ex = AuthorizationException.fromIntent(intent)

        if (response != null) {
            val tokenRequest = response.createTokenExchangeRequest()
            val authService = AuthorizationService(context)

            authService.performTokenRequest(tokenRequest) { tokenResponse, exception ->
                authService.dispose() // Dispose after use
                callback(tokenResponse?.accessToken)
            }
        } else {
            callback(null)
        }
    }

    fun startAuthWithLauncher(activity: Activity, launcher: ActivityResultLauncher<Intent>) {
        val authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            CLIENT_ID,
            ResponseTypeValues.CODE,
            REDIRECT_URI.toUri()
        ).build()

        val authService = AuthorizationService(activity)
        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        launcher.launch(authIntent)
        authService.dispose() // Dispose immediately after getting the intent
    }
}
