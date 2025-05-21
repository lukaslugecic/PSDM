package com.example.psdmclientapp.auth

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit

object TokenStorage {
    private const val PREF_NAME = "secure_prefs"
    private const val KEY_ACCESS_TOKEN  = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"

    private fun prefs(context: Context) = EncryptedSharedPreferences.create(
        context,
        PREF_NAME,
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveTokens(context: Context, accessToken: String, refreshToken: String?) {
        try {
            prefs(context).edit {
                putString(KEY_ACCESS_TOKEN,  accessToken)
                if (refreshToken != null) putString(KEY_REFRESH_TOKEN, refreshToken)
            }
            Log.d("TOKEN_STORAGE", "Tokens saved: access=$accessToken refresh=$refreshToken")
        } catch (e: Exception) {
            Log.e("TOKEN_STORAGE", "Failed to save tokens", e)
        }
    }

    fun getAccessToken(context: Context): String? =
        try {
            prefs(context).getString(KEY_ACCESS_TOKEN, null).also {
                Log.d("TOKEN_STORAGE", "Access token loaded: $it")
            }
        } catch (e: Exception) {
            Log.e("TOKEN_STORAGE", "Failed to load access token", e)
            null
        }

    fun getRefreshToken(context: Context): String? =
        try {
            prefs(context).getString(KEY_REFRESH_TOKEN, null).also {
                Log.d("TOKEN_STORAGE", "Refresh token loaded: $it")
            }
        } catch (e: Exception) {
            Log.e("TOKEN_STORAGE", "Failed to load refresh token", e)
            null
        }
}


