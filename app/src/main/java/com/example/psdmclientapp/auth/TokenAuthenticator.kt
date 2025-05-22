package com.example.psdmclientapp.auth

import android.content.Context
import android.util.Log
import net.openid.appauth.AuthorizationService
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.TokenRequest
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Route
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class TokenAuthenticator(
    private val context: Context,
    private val authService: AuthorizationService
) : Authenticator {

    override fun authenticate(route: Route?, response: okhttp3.Response): Request? {
        // only try once
        if (responseCount(response) > 1) return null

        val refreshToken = TokenStorage.getRefreshToken(context) ?: return null

        // build refresh request
        val tokenReq = TokenRequest.Builder(
            AuthManager.serviceConfig,
            AuthManager.CLIENT_ID
        )
            .setGrantType(GrantTypeValues.REFRESH_TOKEN)
            .setRefreshToken(refreshToken)
            .build()

        // synchronous exchange
        val latch = CountDownLatch(1)
        var newAccess: String? = null
        var newRefresh: String? = null

        authService.performTokenRequest(tokenReq) { resp, ex ->
            if (ex == null && resp != null) {
                newAccess  = resp.accessToken
                newRefresh = resp.refreshToken
            } else {
                Log.e("TokenAuth", "Refresh failed", ex)
            }
            latch.countDown()
        }
        latch.await(5, TimeUnit.SECONDS)

        if (newAccess != null) {
            TokenStorage.saveTokens(context, newAccess!!, newRefresh)
            // use the public getter .request() instead of the field
            val originalReq = response.request
            return originalReq.newBuilder()
                .header("Authorization", "Bearer $newAccess")
                .build()
        }

        return null
    }

    private fun responseCount(response: okhttp3.Response): Int {
        var res: okhttp3.Response? = response
        var cnt = 1
        // use the public getter .priorResponse()
        while (res?.priorResponse != null) {
            cnt++
            res = res.priorResponse
        }
        return cnt
    }
}

