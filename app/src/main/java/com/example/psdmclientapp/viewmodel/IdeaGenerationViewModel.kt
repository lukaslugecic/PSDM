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
import com.example.psdmclientapp.network.ApiClient
import com.example.psdmclientapp.state.ProblemSolvingSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import com.example.psdmclientapp.model.request.AttributeRequest
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlinx.serialization.encodeToString

@HiltViewModel
class IdeaGenerationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sessionId: Long = checkNotNull(savedStateHandle["sessionId"])
    private val problemId: Long = checkNotNull(savedStateHandle["problemId"])

    var state by mutableStateOf(ProblemSolvingSessionState())
        private set

    var nextPage by mutableStateOf<String>("")
        private set

    data class NavigationCommand(
        val route: String
    )

    var navigationCommand by mutableStateOf<NavigationCommand?>(null)
        internal set


    suspend fun loadSession(sessionId: Long): Boolean {
        return try {
            state = state.copy(isLoading = true)

            val sessionDetails = ApiClient.sessionApi.getSessionDetails(sessionId)
            val solutions = ApiClient.solutionApi.getSolutionsBySessionId(sessionId)

            val currentUser = UserResponse(
                3L, "Ime", "Prezime", "Email", LocalDate.now(), 1L
            )

            val problemSolvingMethodId = sessionDetails.body()?.session?.problemSolvingMethodId ?: 1
            val decisionMakingMethodId = sessionDetails.body()?.session?.decisionMakingMethodId ?: 1

            state = state.copy(
                problemTitle = sessionDetails.body()?.problem?.title.toString(),
                problemDescription = sessionDetails.body()?.problem?.description.toString(),
                solutions = solutions,
                isOwner = sessionDetails.body()?.problem?.moderatorId == currentUser.id,
                parentSessionId = sessionDetails.body()?.parentSessionId,
                currentUserId = currentUser.id,
                isLoading = false,
                problemSolvingMethod = ProblemSolvingMethod.fromId(problemSolvingMethodId)
                    ?: ProblemSolvingMethod.BRAINSTORMING,
                decisionMakingMethod = DecisionMakingMethod.fromId(decisionMakingMethodId)
                    ?: DecisionMakingMethod.AVERAGE_WINNER,
                duration = sessionDetails.body()?.session?.duration
            )

            nextPage = when (state.problemSolvingMethod) {
                ProblemSolvingMethod.BRAINSTORMING -> "grouping"
                ProblemSolvingMethod.BRAINWRITING -> "voting"
                ProblemSolvingMethod.SPEEDSTORMING -> "voting"
                ProblemSolvingMethod.NOMINAL_GROUP_TECHNIQUE -> "grouping" // TODO Nominal
            }

            val needsRedirectToSubsession = (state.problemSolvingMethod == ProblemSolvingMethod.BRAINWRITING ||
                    state.problemSolvingMethod == ProblemSolvingMethod.SPEEDSTORMING) &&
                    state.parentSessionId == null

            needsRedirectToSubsession
        } catch (e: Exception) {
            state = state.copy(
                errorMessage = e.localizedMessage,
                isLoading = false
            )
            false
        }
    }



    fun maybeNavigateAfterDelay(attributeTitles: List<String>, problemId: Long, sessionId: Long) {
        viewModelScope.launch {
            state.duration?.let { durationInSeconds ->
                delay(durationInSeconds * 1000)

                if (state.currentUserId == null) return@launch

                val json = Json.encodeToString(attributeTitles)
                val encodedAttributes = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())

                if (state.parentSessionId != null) {
                    val newSessionId = runCatching {
                        ApiClient.userApi.getCurrentSubSessionId(state.currentUserId!!)
                    }.getOrNull()

                    val route = if (newSessionId != null) {
                        "ideaGeneration/$problemId/$newSessionId/$encodedAttributes"
                    } else {
                        "$nextPage/$problemId/${state.parentSessionId}/$encodedAttributes"
                    }

                    navigationCommand = NavigationCommand(route)
                } else {
                    val route = "$nextPage/$problemId/$sessionId/$encodedAttributes"
                    navigationCommand = NavigationCommand(route)
                }
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

    fun submitSolutionWithAttributes(title: String, attributes: List<Pair<String, String>>) {
        viewModelScope.launch {
            try {
                val attributeRequests = attributes
                    .filter { it.first.isNotBlank() && it.second.isNotBlank() }
                    .map { (key, value) -> AttributeRequest(title = key, value = value) }

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

}
