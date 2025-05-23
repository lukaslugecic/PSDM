package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.network.UserApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val userApi: UserApiService
) : ViewModel() {

    // hold the loaded current user (or null while loading / on error)
    var currentUserId by mutableStateOf<Long?>(null)
        private set

    // an error message to surface if no session is found
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        // fetch “/user/me” right away
        viewModelScope.launch {
            try {
                currentUserId = userApi.getCurrentUser().id
            } catch (e: Exception) {
                // handle this gracefully if you want
                errorMessage = "Failed to load current user"
            }
        }
    }

    /** Call this when the “Join Session” button is tapped */
    fun onJoinSessionRequested(
        onNavigate: (subSessionPath: String) -> Unit
    ) {
        val myId = currentUserId ?: return
        viewModelScope.launch {
            try {
                val resp = userApi.getCurrentSubSessionUrl(myId)
                if (resp.isSuccessful) {
                    val path = resp.body().orEmpty()
                    if (path.isBlank()) {
                        errorMessage = "No active sessions"
                    } else {
                        onNavigate(path)
                    }
                } else {
                    errorMessage = "No active sessions"
                }
            } catch (e: Exception) {
                errorMessage = "No active sessions2000 myid $myId"
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}

