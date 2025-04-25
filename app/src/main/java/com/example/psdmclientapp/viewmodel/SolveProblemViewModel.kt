package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.psdmclientapp.model.SessionRequest
import com.example.psdmclientapp.model.SessionResponse
import com.example.psdmclientapp.model.ProblemRequest
import com.example.psdmclientapp.network.ApiClient
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.model.DecisionMakingMethodDTO
import com.example.psdmclientapp.model.ProblemSolvingMethodDTO
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.Long

@HiltViewModel
class SolveProblemViewModel @Inject constructor() : ViewModel() {

    var title by mutableStateOf("")
    var description by mutableStateOf("")

    var selectedSolvingMethodId by mutableStateOf<Long?>(null)
    var selectedDecisionMethodId by mutableStateOf<Long?>(null)

    var problemSolvingMethods by mutableStateOf<List<ProblemSolvingMethodDTO>>(emptyList())
    var decisionMakingMethods by mutableStateOf<List<DecisionMakingMethodDTO>>(emptyList())

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        fetchMethods()
    }

    private fun fetchMethods() {
        viewModelScope.launch {
            try {
                problemSolvingMethods = ApiClient.methodApiService.getSolvingSolvingMethods()
                decisionMakingMethods = ApiClient.methodApiService.getDecisionMakingMethods()
            } catch (e: Exception) {
                errorMessage = "Failed to load methods: ${e.message}"
            }
        }
    }



    suspend fun submit(onSuccess: (SessionResponse) -> Unit) {
        try {
            isLoading = true
            errorMessage = null

            val now = LocalDateTime.now().toString()

            val problem = ApiClient.problemApi.createProblem(
                ProblemRequest(
                    title = title,
                    description = description,
                    moderatorId = 1L, // Replace with dynamic moderator ID if needed
                    start = now,
                    end = now
                )
            )

            val session = ApiClient.sessionApi.createSession(
                SessionRequest(
                    problemId = problem.id,
                    problemSolvingMethodId = selectedSolvingMethodId!!,
                    decisionMakingMethodId = selectedDecisionMethodId!!,
                    start = now,
                    end = now
                )
            )

            onSuccess(session)

        } catch (e: Exception) {
            errorMessage = e.localizedMessage ?: "Unexpected error"
        } finally {
            isLoading = false
        }
    }
}
