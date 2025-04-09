package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.model.Participant
import com.example.psdmclientapp.model.SessionInfo
import com.example.psdmclientapp.model.User
import com.example.psdmclientapp.network.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionLobbyViewModel @Inject constructor() : ViewModel() {

    var sessionInfo by mutableStateOf<SessionInfo?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadSession(sessionId: Long) {
        viewModelScope.launch {
            isLoading = true
            try {
                //val response = ApiClient.sessionApi.getSessionInfo(sessionId)
                //sessionInfo = response.body()
                sessionInfo = SessionInfo(
                    id = 1L,
                    problemTitle = "Što ručamo?",
                    solvingMethod = "Brainstorming",
                    decisionMethod = "Borda rangiranje",
                    isOwner = true,
                    participants =  listOf(
                        Participant(1, "Ivan", true),
                        Participant(2, "Ana", false),
                        Participant(3, "Petra", true),
                        Participant(4, "Marko", true)
                    )
                )
            } catch (e: Exception) {
                errorMessage = "Ne mogu dohvatiti informacije o sesiji"
            } finally {
                isLoading = false
            }
        }
    }

    fun refreshSession() {
        sessionInfo?.id?.let { loadSession(it) }
    }

    fun startSession(sessionId: Long) {
        viewModelScope.launch {
            try {
                ApiClient.sessionApi.startSession(sessionId)
            } catch (e: Exception) {
                errorMessage = "Neuspješno pokretanje sesije"
            }
        }
    }
}
