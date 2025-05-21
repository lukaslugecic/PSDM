package com.example.psdmclientapp.network

import android.content.Context
import com.example.psdmclientapp.auth.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        TokenStorage.getAccessToken(context)?.let { token ->
            val authReq = original.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            return chain.proceed(authReq)
        }
        return chain.proceed(original)
    }
}

