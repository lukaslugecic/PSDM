package com.example.psdmclientapp.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SessionLobbyViewModel @Inject constructor() : ViewModel() {

//    var sessionInfo by mutableStateOf<SessionDetails?>(null)
//    var isLoading by mutableStateOf(false)
//    var errorMessage by mutableStateOf<String?>(null)
//
//    fun loadSession(sessionId: Long) {
//        viewModelScope.launch {
//            isLoading = true
//            try {
//                //val response = ApiClient.sessionApi.getSessionInfo(sessionId)
//                //sessionInfo = response.body()
//                 sessionInfo = SessionInfo(
//                    id = 1L,
//                    problemTitle = "Što ručamo?",
//                    solvingMethod = "Brainstorming",
//                    decisionMethod = "Borda rangiranje",
//                    isOwner = true,
//                    participants =  listOf(
//                        Participant(1, "Ivan", true),
//                        Participant(2, "Ana", false),
//                        Participant(3, "Petra", true),
//                        Participant(4, "Marko", true)
//                    )
//                )
//            } catch (e: Exception) {
//                errorMessage = "Ne mogu dohvatiti informacije o sesiji"
//            } finally {
//                isLoading = false
//            }
//        }
//    }
//
//    fun refreshSession() {
//        sessionInfo?.id?.let { loadSession(it) }
//    }
//
//    fun startSession(sessionId: Long) {
//        viewModelScope.launch {
//            try {
//                ApiClient.sessionApi.startSession(sessionId)
//            } catch (e: Exception) {
//                errorMessage = "Neuspješno pokretanje sesije"
//            }
//        }
//    }
}
