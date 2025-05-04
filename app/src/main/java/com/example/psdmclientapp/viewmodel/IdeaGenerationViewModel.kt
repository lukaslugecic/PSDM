package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.enum.DecisionMakingMethod
import com.example.psdmclientapp.enum.ProblemSolvingMethod
import com.example.psdmclientapp.model.request.SolutionRequest
import com.example.psdmclientapp.model.UserResponse
import com.example.psdmclientapp.model.request.GroupSolutionRequest
import com.example.psdmclientapp.network.ApiClient
import com.example.psdmclientapp.state.ProblemSolvingSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import android.util.Log
import com.example.psdmclientapp.model.StepDetailsResponse
import com.example.psdmclientapp.model.request.AttributeRequest
import java.time.Duration

@HiltViewModel
class IdeaGenerationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sessionId: Long = checkNotNull(savedStateHandle["sessionId"])
    private val problemId: Long = checkNotNull(savedStateHandle["problemId"])

    var state by mutableStateOf(ProblemSolvingSessionState())
        private set

    var shouldRedirectToSubsession by mutableStateOf(false)
        private set

    var rotationDurationInSeconds by mutableStateOf<Long?>(null)


    fun loadSession(sessionId: Long) {
        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true)

                val sessionDetails = ApiClient.sessionApi.getSessionDetails(sessionId)
                val solutions = ApiClient.solutionApi.getSolutionsBySessionId(sessionId)
                //val currentUser = ApiClient.authApi.getCurrentUser();

                val currentUser = UserResponse(
                    2L,
                    "Ime",
                    "Prezime",
                    "Email",
                    LocalDate.now(),
                    1L
                )

                val problemSolvingMethodId = sessionDetails.body()?.session?.problemSolvingMethodId ?: 1
                val decisionMakingMethodId = sessionDetails.body()?.session?.decisionMakingMethodId ?: 1


                state = state.copy(
                    problemTitle = sessionDetails.body()?.problem?.title.toString(),
                    problemDescription = sessionDetails.body()?.problem?.description.toString(),
                    solutions = solutions,
                    isOwner = sessionDetails.body()?.problem?.moderatorId == currentUser.id,
                    currentUserId = currentUser.id,
                    isLoading = false,
                    problemSolvingMethod = ProblemSolvingMethod.fromId(problemSolvingMethodId) ?: ProblemSolvingMethod.BRAINSTORMING,
                    decisionMakingMethod = DecisionMakingMethod.fromId(decisionMakingMethodId) ?: DecisionMakingMethod.AVERAGE_WINNER
                )


                val steps = sessionDetails.body()?.steps

                var documentRotationStep: StepDetailsResponse? = null

                when(state.problemSolvingMethod){
                    ProblemSolvingMethod.BRAINSTORMING -> {

                    }
                    ProblemSolvingMethod.BRAINWRITING -> {
                        documentRotationStep = steps?.find { it.title == "Document Rotation"}
                    }
                    ProblemSolvingMethod.SPEEDSTORMING -> {
                        documentRotationStep = steps?.find { it.title == "Rapid Pair Rotation"}
                    }
                    ProblemSolvingMethod.NOMINAL_GROUP_TECHNIQUE -> {

                    }
                }

                rotationDurationInSeconds = try {
                    documentRotationStep?.duration?.let { Duration.parse(it).seconds }
                } catch (e: Exception) {
                    Log.e("ParseDuration", "Invalid duration format: ${documentRotationStep?.duration}", e)
                    null
                }

                if ((state.problemSolvingMethod == ProblemSolvingMethod.BRAINWRITING ||
                            state.problemSolvingMethod == ProblemSolvingMethod.SPEEDSTORMING) &&
                    sessionDetails.body()?.isSubSession == false) {
                    shouldRedirectToSubsession = true
                }


            } catch (e: Exception) {
                state = state.copy(
                    errorMessage = e.localizedMessage,
                    isLoading = false
                )
            }
        }
    }
    fun refreshSolutions() {
        viewModelScope.launch {
            try {
                val solutions = ApiClient.solutionApi.getSolutionsBySessionId(sessionId)
                state = state.copy(solutions = solutions)
            } catch (e: Exception) {
                state = state.copy(errorMessage = e.localizedMessage)
            }
        }
    }

    fun submitSolution(text: String) {
        viewModelScope.launch {
            try {
                val newSolution = ApiClient.solutionApi.submitSolution(
                    SolutionRequest(
                        text,
                        state.currentUserId,
                        problemId,
                        sessionId
                    )
                )
                state = state.copy(
                    solutions = state.solutions + newSolution
                )
            } catch (e: Exception) {
                state = state.copy(errorMessage = e.localizedMessage)
            }
        }
    }

    fun submitSolutionWithAttributes(title: String, attributes: List<Pair<String, String>>) {
        viewModelScope.launch {
            try {
                val attributeRequests = attributes.map { (key, value) ->
                    AttributeRequest(title = key, value = value)
                }

                println("")

                val newSolution = ApiClient.solutionApi.submitSolution(
                    SolutionRequest(
                        title = title,
                        userId = state.currentUserId,
                        problemId = problemId,
                        sessionId = sessionId,
                        attributes = attributeRequests
                    )
                )
                state = state.copy(
                    solutions = state.solutions + newSolution
                )
            } catch (e: Exception) {
                state = state.copy(errorMessage = e.localizedMessage)
            }
        }
    }


    fun endProblemSolving() {
        viewModelScope.launch {
            try {
               // ApiClient.sessionApi.endProblemSolving(sessionId)
                println("endam ti mamu")
            } catch (e: Exception) {
                state = state.copy(errorMessage = e.localizedMessage)
            }
        }
    }

    fun submitGroupedSolution(newTitle: String, solutionIds: List<Long>) {
        println("submit")
        viewModelScope.launch {
            try {
                val combinedText = state.solutions
                    .filter { solutionIds.contains(it.id) }
                    .joinToString(separator = "\n") { "- ${it.title}" }

                val newSolution = ApiClient.solutionApi.groupSolutions(
                    GroupSolutionRequest(
                        solutionIds,
                        SolutionRequest(
                            title = newTitle,
                            userId = state.currentUserId,
                            problemId = problemId,
                            sessionId = sessionId
                        )
                    )
                )

                state = state.copy(solutions = state.solutions + newSolution)

            } catch (e: Exception) {
                state = state.copy(errorMessage = e.localizedMessage)
            }
        }
    }

    fun refreshTurn() {
        viewModelScope.launch {
            try {
                val turnUserId = ApiClient.sessionApi.getCurrentTurnUserId(sessionId)
                val isMyTurn = turnUserId == state.currentUserId
                state = state.copy(
                    currentUserId = turnUserId,
                    isCurrentTurn = isMyTurn
                )
            } catch (e: Exception) {
                state = state.copy(errorMessage = e.localizedMessage)
            }
        }
    }

    fun submitTurnIdea(title: String) {
        if (!state.isCurrentTurn) {
            state = state.copy(errorMessage = "Nisi na redu!")
            return
        }

        viewModelScope.launch {
            try {
                val newSolution = ApiClient.solutionApi.submitSolution(
                    SolutionRequest(
                        title,
                        state.currentUserId,
                        problemId,
                        sessionId
                    )
                )
                state = state.copy(
                    solutions = state.solutions + newSolution
                )

                // Napredak u krugu
                ApiClient.sessionApi.advanceTurn(sessionId)
                refreshTurn()
            } catch (e: Exception) {
                state = state.copy(errorMessage = e.localizedMessage)
            }
        }
    }


}
