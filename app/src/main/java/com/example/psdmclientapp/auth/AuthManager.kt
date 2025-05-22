
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
    private const val END_SESSION_ENDPOINT = "http://192.168.186.220:8080/realms/psdm-realm/protocol/openid-connect/logout"


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
        TOKEN_ENDPOINT.toUri(),
        null,
        END_SESSION_ENDPOINT.toUri()
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
        )
            .setScope("openid profile email offline_access")
            .build()

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

        response.idToken?.let { TokenStorage.saveIdToken(context, it) }


        Log.d("AUTH_MANAGER", "Authorization response received: $response")
        Log.d("AUTH_MANAGER", "Authorization response id token received: ${response.idToken}")
        pendingAuthRequest = null

        // process:com.example.psdmclientapp tag:TOKEN_STORAGE tag:AUTH_MANAGER tag:AUTH_LAUNCHER tag:HTTP_TEST
        val tokenRequest = response.createTokenExchangeRequest()

        val authService = AuthorizationService(context, insecureConfig)
        try {
            authService.performTokenRequest(tokenRequest) { tokenResponse, ex ->
                if (tokenResponse != null) {
                    // ① Save the ID token that Keycloak returned here:
                    tokenResponse.idToken?.let { idToken ->
                        Log.d("AUTH_MANAGER", "ID token from token endpoint: $idToken")
                        TokenStorage.saveIdToken(context, idToken)
                    }

                    // ② Save your access + refresh tokens as before
                    TokenStorage.saveTokens(
                        context,
                        tokenResponse.accessToken!!,
                        tokenResponse.refreshToken
                    )

                    callback(tokenResponse.accessToken)
                } else {
                    Log.e("AUTH_MANAGER", "token exchange error", ex)
                    callback(null)
                }
                authService.dispose()
            }
        } catch (e: Exception) {
            Log.e("AUTH_MANAGER", "performTokenRequest threw", e)
            callback(null)
        }
    }


    /**
     * Returns an Intent you can launch to do a Keycloak logout (end‐session) flow.
     * Keycloak must be configured with this post‐logout redirect URI.
     */
    fun getEndSessionIntent(context: Context): Intent {
        // 1) Grab the last ID token you saved (or null if none)
        val idTokenHint = TokenStorage.getIdToken(context)

        // 2) Build the EndSessionRequest *with both* id token hint AND redirect URI
        val request = EndSessionRequest.Builder(serviceConfig)
            .setIdTokenHint(idTokenHint)                        // must set this
            .setPostLogoutRedirectUri(REDIRECT_URI.toUri())     // *must* set this too!
            .build()

        // 3) Return the browser Intent
        return AuthorizationService(context, insecureConfig)
            .getEndSessionRequestIntent(request)
    }


    /**
     * Call this from your Activity's `onNewIntent` to complete the logout flow.
     * After this, the app should clear tokens and navigate to login.
     */
    fun handleEndSessionResponse(intent: Intent, onComplete: () -> Unit) {
        val resp = EndSessionResponse.fromIntent(intent)
        val ex   = AuthorizationException.fromIntent(intent)
        if (resp != null) {
            Log.d("AUTH_MANAGER", "EndSessionResponse OK")
        } else {
            Log.e("AUTH_MANAGER", "EndSession failed", ex)
        }
        onComplete()
    }
}




