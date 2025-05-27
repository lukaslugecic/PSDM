package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.psdmclientapp.model.SessionDetailsResponse
import com.example.psdmclientapp.network.SessionApiService

@HiltViewModel
class ProblemSessionsViewModel @Inject constructor(
    private val sessionApi: SessionApiService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Retrieve the problemId from navigation arguments
    private val problemId: Long = savedStateHandle.get<Long>("problemId")
        ?: throw IllegalArgumentException("Problem ID is missing in SavedStateHandle")

    // UI state
    var sessions by mutableStateOf<List<SessionDetailsResponse>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchSessions()
    }

    private fun fetchSessions() {
        viewModelScope.launch {
            try {
                isLoading = true
                sessions = sessionApi.getSessionsForProblem(problemId)

            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
            } finally {
                isLoading = false
            }
        }
    }
}
