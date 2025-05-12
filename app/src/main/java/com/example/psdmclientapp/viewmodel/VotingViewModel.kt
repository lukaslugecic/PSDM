package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.enum.DecisionMakingMethod
import com.example.psdmclientapp.model.request.VoteRequest
import com.example.psdmclientapp.network.ApiClient
import com.example.psdmclientapp.state.VotingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VotingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val sessionId : Long = checkNotNull(savedStateHandle["sessionId"])

    var state by mutableStateOf(VotingState())
        private set

    init {
        loadSolutions()
    }

    fun loadSolutions() {
        viewModelScope.launch {
            val session = ApiClient.sessionApi.getSessionDetails(sessionId)
            val solutions = ApiClient.solutionApi.getSolutionsByParentSessionIdOrSessionId(sessionId)

            val decisionMethod = session.body()?.session?.decisionMakingMethodId?.let {
                DecisionMakingMethod.entries.first { m -> m.id == it }
            } ?: DecisionMakingMethod.MAJORITY_RULE

            state = state.copy(
                currentUserId = 2L,
                solutions = solutions,
                decisionMethod = decisionMethod
            )
        }
    }

    fun setRating(id: Long, value: Int) {
        state = state.copy(ratings = state.ratings.toMutableMap().apply {
            put(id, value)
        })
    }

    fun submitVotes(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val votes = state.ratings.map { (solutionId, rating) ->
                    VoteRequest(
                        userId = state.currentUserId!!,
                        solutionId = solutionId,
                        value = rating.toDouble()
                    )
                }
                ApiClient.voteApi.submitVotes(votes)

//                while (true) {
//                    val allVoted = ApiClient.voteApi.haveAllUsersVoted(sessionId)
//                    if (allVoted) {
//                        state = state.copy(shouldNavigateToResult = true)
//                        break
//                    }
//                    delay(3000) // Poll every 3 seconds
//                }

                onSuccess()
            } catch (e: Exception) {
                state = state.copy(errorMessage = e.localizedMessage)
            }
        }
    }

}
