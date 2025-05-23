package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.enum.DecisionMakingMethod
import com.example.psdmclientapp.model.request.VoteRequest
import com.example.psdmclientapp.network.SessionApiService
import com.example.psdmclientapp.network.SolutionApiService
import com.example.psdmclientapp.network.UserApiService
import com.example.psdmclientapp.network.VoteApiService
import com.example.psdmclientapp.state.VotingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VotingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val voteApi: VoteApiService,
    private val solutionApi: SolutionApiService,
    private val sessionApi: SessionApiService,
    private val userApi: UserApiService
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
            val session = sessionApi.getSessionDetails(sessionId)
            var solutions = solutionApi.getSolutionsByParentSessionIdOrSessionId(sessionId).sortedBy { it.title.lowercase() }

            var user = userApi.getCurrentUser()

            val decisionMethod = session.body()?.session?.decisionMakingMethodId?.let {
                DecisionMakingMethod.entries.first { m -> m.id == it }
            } ?: DecisionMakingMethod.MAJORITY_RULE

            // only auto-vote if you’re the moderator AND exactly 3 solutions
            if (session.body()?.problem?.moderatorId == user.id && solutions.size == 3) {
                // pick the right hard-coded vote values

                val votesValues1 = when (decisionMethod) {
                    DecisionMakingMethod.AVERAGE_WINNER,
                    DecisionMakingMethod.WEIGHTED_AVERAGE_WINNER ->
                        listOf(5.0, 4.0, 1.0)

                    DecisionMakingMethod.BORDA_RANKING ->
                        listOf(1.0, 2.0, 3.0)

                    DecisionMakingMethod.MAJORITY_RULE ->
                        listOf(1.0, 0.0, 0.0)
                }

                val voteRequests1 = solutions.mapIndexed { index, sol ->
                    VoteRequest(
                        userId     = 1L,
                        solutionId = sol.id,
                        value      = votesValues1[index]
                    )
                }

                val votesValues2 = when (decisionMethod) {
                    DecisionMakingMethod.AVERAGE_WINNER,
                    DecisionMakingMethod.WEIGHTED_AVERAGE_WINNER ->
                        listOf(2.0, 3.0, 4.0)

                    DecisionMakingMethod.BORDA_RANKING ->
                        listOf(3.0, 2.0, 1.0)

                    DecisionMakingMethod.MAJORITY_RULE ->
                        listOf(0.0, 0.0, 1.0)
                }

                val voteRequests2 = solutions.mapIndexed { index, sol ->
                    VoteRequest(
                        userId     = 2L,
                        solutionId = sol.id,
                        value      = votesValues2[index]
                    )
                }

                val allVoteRequests: List<VoteRequest> = voteRequests1 + voteRequests2

                voteApi.submitVotes(allVoteRequests)
            }

            val initialRatings = solutions.associate { it.id to 3 }.toMutableMap()
            state = state.copy(
                maxDuration = session.body()?.session?.duration,
                ratings = initialRatings,
                currentUserId = user.id,
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
                voteApi.submitVotes(votes)

                waintingEveryoneToVote = true

                val maxDurationMillis = (state.maxDuration ?: 60L) * 1000  // default to 60s if null
                val startTime = System.currentTimeMillis()

                while (true) {
                    val allVoted = voteApi.haveAllUsersVoted(sessionId)
                    if (allVoted) break

                    val elapsed = System.currentTimeMillis() - startTime
                    if (elapsed >= maxDurationMillis) {
                        state = state.copy(errorMessage = "Timed out waiting for all users to vote.")
                        break
                    }

                    kotlinx.coroutines.delay(3000)
                }

                waintingEveryoneToVote = false
                onNavigate()
            } catch (e: Exception) {
                waintingEveryoneToVote = false
                state = state.copy(errorMessage = e.localizedMessage)
            }
        }
    }


}
