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
import com.example.psdmclientapp.state.ProblemSolvingSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.psdmclientapp.model.request.AttributeRequest
import com.example.psdmclientapp.network.SessionApiService
import com.example.psdmclientapp.network.SolutionApiService
import com.example.psdmclientapp.network.UserApiService
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlinx.serialization.encodeToString

@HiltViewModel
class IdeaGenerationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionApi: SessionApiService,
    private val userApi: UserApiService,
    private val solutionApi: SolutionApiService,
) : ViewModel() {

    private val sessionId: Long = checkNotNull(savedStateHandle["sessionId"])
    private val problemId: Long = checkNotNull(savedStateHandle["problemId"])

    var state by mutableStateOf(ProblemSolvingSessionState())
        private set

    var nextPage by mutableStateOf<String>("")
        private set

    var isWaitingForSession by mutableStateOf(false)
        private set

    data class NavigationCommand(
        val route: String
    )

    var navigationCommand by mutableStateOf<NavigationCommand?>(null)
        internal set


    suspend fun loadSession(sessionId: Long): Boolean {
        return try {
            state = state.copy(isLoading = true)

            val sessionDetails = sessionApi.getSessionDetails(sessionId)
            val solutions = solutionApi.getSolutionsBySessionId(sessionId)

            val currentUser = userApi.getCurrentUser()

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



    fun navigateAfterDelay(attributeTitles: List<String>, problemId: Long, sessionId: Long) {
        viewModelScope.launch {
            state.duration?.let { durationInSeconds ->
                delay(durationInSeconds * 1000)

                if (state.currentUserId == null) return@launch

                val json = Json.encodeToString(attributeTitles)
                val encodedAttributes = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())

                if (state.parentSessionId != null) {
                    var newSessionId: Long? = null
                    var parentStillRunning = true

                    isWaitingForSession = true

                    // Repeat check until parent session ends or subsession is found
                    while (parentStillRunning && newSessionId == null) {
                        // Try to get subsession
                        newSessionId = runCatching {
                            userApi.getCurrentSubSessionId(state.currentUserId!!)
                        }.getOrNull()

                        if (newSessionId == null) {
                            // Check if parent is still running
                            parentStillRunning = runCatching {
                                userApi.checkParentSession(state.currentUserId!!)
                            }.getOrDefault(false)

                            if (parentStillRunning) {

                                delay(durationInSeconds * 1000)
                            }
                        }
                    }

                    isWaitingForSession = false

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

    suspend fun shouldRedirect(attributeTitles: List<String>,) {
        val shouldRedirect = loadSession(sessionId)

        if (shouldRedirect && state.currentUserId != null) {
            val json = Json.encodeToString(attributeTitles)
            val encodedAttributes = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
            val newSessionId = userApi.getCurrentSubSessionId(state.currentUserId!!)

            val route = "ideaGeneration/$problemId/$newSessionId/$encodedAttributes"
            navigationCommand = NavigationCommand(route)
        } else {
            navigateAfterDelay(attributeTitles, problemId, sessionId)
        }
    }



    fun refreshSolutions() {
        viewModelScope.launch {
            try {
                val solutions = solutionApi.getSolutionsBySessionId(sessionId)
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

                val newSolution = solutionApi.submitSolution(
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
