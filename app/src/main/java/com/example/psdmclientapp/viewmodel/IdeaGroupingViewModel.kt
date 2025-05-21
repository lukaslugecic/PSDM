package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.model.request.SolutionRequest
import com.example.psdmclientapp.model.UserResponse
import com.example.psdmclientapp.model.request.GroupSolutionRequest
import com.example.psdmclientapp.state.ProblemSolvingSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import com.example.psdmclientapp.model.request.AttributeRequest
import com.example.psdmclientapp.network.SessionApiService
import com.example.psdmclientapp.network.SolutionApiService
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@HiltViewModel
class IdeaGroupingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionApi: SessionApiService,
    private val solutionApi: SolutionApiService,
) : ViewModel() {

    private val sessionId: Long = checkNotNull(savedStateHandle["sessionId"])
    private val problemId: Long = checkNotNull(savedStateHandle["problemId"])

    var state by mutableStateOf(ProblemSolvingSessionState())
        private set

    data class NavigationCommand(
        var route: String
    )

    var navigationCommand by mutableStateOf<NavigationCommand?>(null)
        internal set

    suspend fun loadSession(sessionId: Long) {
         try {
            state = state.copy(isLoading = true)

            val sessionDetails = sessionApi.getSessionDetails(sessionId)
            val solutions = solutionApi.getSolutionsByParentSessionIdOrSessionId(sessionId)

            val currentUser = UserResponse(
                3L, "Ime", "Prezime", "Email", LocalDate.now(), 1L
            )

            state = state.copy(
                duration = sessionDetails.body()?.session?.duration,
                solutions = solutions,
                currentUserId = currentUser.id,
                isLoading = false,
            )

        } catch (e: Exception) {
            state = state.copy(
                errorMessage = e.localizedMessage,
                isLoading = false
            )
        }
    }

    fun navigateAfterDelay(attributeTitles: List<String>, problemId: Long, sessionId: Long) {
        viewModelScope.launch {
            state.duration?.let { durationInSeconds ->
                delay(durationInSeconds * 1000)

                val json = Json.encodeToString(attributeTitles)
                val encodedAttributes = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                navigationCommand = NavigationCommand("voting/$problemId/$sessionId/$encodedAttributes")

            }
        }
    }

    fun refreshSolutions() {
        viewModelScope.launch {
            try {
                val solutions = solutionApi.getSolutionsByParentSessionIdOrSessionId(sessionId)
                state = state.copy(solutions = solutions)
            } catch (e: Exception) {
                state = state.copy(errorMessage = e.localizedMessage)
            }
        }
    }

    fun submitGroupedSolution(newTitle: String, solutionIds: List<Long>, attributes: List<Pair<String, String>>) {
        viewModelScope.launch {
            try {
                val attributeRequests = attributes
                    .filter { it.first.isNotBlank() && it.second.isNotBlank() }
                    .map { (key, value) -> AttributeRequest(title = key, value = value) }

                val newSolution = solutionApi.groupSolutions(
                    GroupSolutionRequest(
                        solutionIds,
                        SolutionRequest(
                            title = newTitle,
                            userId = state.currentUserId,
                            problemId = problemId,
                            sessionId = sessionId,
                            attributeRequests
                        )
                    )
                )

                state = state.copy(solutions = state.solutions + newSolution)

            } catch (e: Exception) {
                state = state.copy(errorMessage = e.localizedMessage)
            }
        }
    }
}
