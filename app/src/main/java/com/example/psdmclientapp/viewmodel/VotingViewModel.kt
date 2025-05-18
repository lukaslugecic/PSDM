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

    var waintingEveryoneToVote by mutableStateOf(false)
        private set

    init {
        loadSolutions()
    }

    fun loadSolutions() {
        viewModelScope.launch {
            val session = ApiClient.sessionApi.getSessionDetails(sessionId)
            var solutions = ApiClient.solutionApi.getSolutionsByParentSessionIdOrSessionId(sessionId)

            val decisionMethod = session.body()?.session?.decisionMakingMethodId?.let {
                DecisionMakingMethod.entries.first { m -> m.id == it }
            } ?: DecisionMakingMethod.MAJORITY_RULE

            val initialRatings = solutions.associate { it.id to 3 }.toMutableMap()
            state = state.copy(
                ratings = initialRatings,
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


    fun submitVotes(onNavigate: () -> Unit) {
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

//                // Start waiting
//                waintingEveryoneToVote = true
//
//                while (true) {
//                    val allVoted = ApiClient.voteApi.haveAllUsersVoted(sessionId)
//                    if (allVoted) break
//                    kotlinx.coroutines.delay(3000)
//                }
//
//                waintingEveryoneToVote = false
                onNavigate()
            } catch (e: Exception) {
                waintingEveryoneToVote = false
                state = state.copy(errorMessage = e.localizedMessage)
            }
        }
    }

}
