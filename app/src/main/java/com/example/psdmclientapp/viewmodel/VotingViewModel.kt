package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.enum.DecisionMakingMethod
import com.example.psdmclientapp.model.SolutionResponse
import com.example.psdmclientapp.network.ApiClient
import com.example.psdmclientapp.state.VotingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject


@HiltViewModel
class VotingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val sessionId : Long = checkNotNull(savedStateHandle["sessionId"])

    var state by mutableStateOf(VotingState())
        private set

    var winningSolution by mutableStateOf<SolutionResponse?>(null)
        private set

    init {
        loadSolutions()
        determineWinner()
    }

    fun loadSolutions() {
        viewModelScope.launch {
            // Fetch solutions & decision method for this session
            val session = ApiClient.sessionApi.getSessionDetails(sessionId)
            val solutions = ApiClient.solutionApi.getSolutionsBySessionId(sessionId)

            val decisionMethod = session.body()?.session?.decisionMakingMethodId?.let {
                DecisionMakingMethod.entries.first { m -> m.id == it }
            } ?: DecisionMakingMethod.MAJORITY_RULE

            state = state.copy(
                solutions = solutions,
                decisionMethod = decisionMethod,
                ranking = solutions.map { it.id }
            )
        }
    }

    fun setRating(id: Long, value: Int) {
        state = state.copy(ratings = state.ratings.toMutableMap().apply {
            put(id, value)
        })
    }

    fun submitVotes() {
        viewModelScope.launch {
            try {
                // Send appropriate request depending on method
                when (state.decisionMethod) {
                    DecisionMakingMethod.MAJORITY_RULE -> {
                        // send state.selectedSolutionId
                    }
                    DecisionMakingMethod.AVERAGE_WINNER -> {
                        // send state.ratings
                    }
                    DecisionMakingMethod.BORDA_RANKING -> {
                        // send state.ranking
                    }

                    DecisionMakingMethod.WEIGHTED_AVERAGE_WINNER -> TODO()
                }
            } catch (e: Exception) {
                state = state.copy(errorMessage = e.localizedMessage)
            }
        }
    }

    fun determineWinner() {
        // Assume winner has been calculated already
        winningSolution = SolutionResponse(
            1L,
            "Najbolje rešenje",
            "Opis",
            1L,
            1L,
            1L,
            LocalDateTime.now().toString(),
            true
        )
    }

    fun startNewSession() {

    }

    fun returnHome() {

    }
}
