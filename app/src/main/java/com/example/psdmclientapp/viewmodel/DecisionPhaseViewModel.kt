package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.model.DecisionPhaseState
import com.example.psdmclientapp.model.Solution
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DecisionPhaseViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(DecisionPhaseState())
        private set

    fun loadSession(sessionId: Long) {
        viewModelScope.launch {
            try {
//                val response = apiClient..getDecisionPhase(sessionId)
//                response.body()?.let {
//                    state = it.copy(sessionId = sessionId)
//                }
                state = DecisionPhaseState (
                    sessionId = 1L,
                    decisionMethod = "Average Winner",
                    ideas = listOf(
                        Solution (
                            1L, "Jabuka", "Zlatni delišes", 1L, 1L, 1L, "srt", false
                        ),
                        Solution (
                            2L, "Kruska", "Zlatni delišes", 1L, 1L, 1L, "srt", false
                        ),
                        Solution (
                            3L, "Pizza", "Zlatni delišes", 1L, 1L, 1L, "srt", false
                        )
                    ),
                    ratings = emptyMap(),
                    errorMessage = null
                )
            } catch (e: Exception) {
                state = state.copy(errorMessage = "Greška: ${e.localizedMessage}")
            }
        }
    }

    fun setRating(ideaId: Long, value: Int) {
        state = state.copy(
            ratings = state.ratings.toMutableMap().apply { put(ideaId, value) }
        )
    }

    fun submitRatings() {
        viewModelScope.launch {
            try {
              //  api.submitRatings(state.sessionId!!, state.ratings)
            } catch (e: Exception) {
                state = state.copy(errorMessage = "Greška pri slanju")
            }
        }
    }
}
