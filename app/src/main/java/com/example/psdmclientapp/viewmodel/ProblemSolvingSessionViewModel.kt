package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.model.Solution
import com.example.psdmclientapp.model.ProblemSolvingSessionState
import com.example.psdmclientapp.network.ApiClient
import com.example.psdmclientapp.network.ApiClient.solutionApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.Long

@HiltViewModel
class ProblemSolvingSessionViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(ProblemSolvingSessionState())
        private set

    fun loadSession(sessionId: Long) {
        viewModelScope.launch {
            try {
                //val response = ApiClient.solutionApi.getProblemSolvingSession(sessionId)
                val response = ProblemSolvingSessionState(
                    sessionId = 1L,
                    problemTitle = "Što ručamo?",
                    problemDescription = "Gladni smo, a ne znamo što jesti.",
                    solutions = emptyList(),
                    isOwner = true,
                    errorMessage = null
                )
               // state = response.body() ?: state.copy(errorMessage = "Greška pri dohvaćanju sesije")
                state = response
            } catch (e: Exception) {
                state = state.copy(errorMessage = "Greška: ${e.localizedMessage}")
            }
        }
    }

    fun refreshSolutions() {
        state.sessionId?.let {
            loadSession(it)
        }
    }

    fun submitSolution(content: String) {
        viewModelScope.launch {
            try {
              //  ApiClient.solutionApi.submitSolution(state.sessionId!!, content)
                val newSolution = Solution (
                    id = 1L,
                    title = content,
                    description = "desc",
                    userId = 1L,
                    problemId = 1L,
                    sessionId = 1L,
                    createdTime = LocalDateTime.now().toString(),
                    chosen = false
                )
                // Kreirajte novu listu s dodanim rješenjem
                val updatedSolutions = state.solutions + newSolution

                // Ažurirajte state s novom listom
                state = state.copy(solutions = updatedSolutions)
              //  refreshSolutions()
            } catch (e: Exception) {
                state = state.copy(errorMessage = "Ne mogu poslati ideju")
            }
        }
    }

    fun endProblemSolving() {
        viewModelScope.launch {
            try {
                //ApiClient.solutionApi.endProblemSolving(state.sessionId!!)
            } catch (e: Exception) {
                state = state.copy(errorMessage = "Ne mogu završiti brainstorming")
            }
        }
    }
}
