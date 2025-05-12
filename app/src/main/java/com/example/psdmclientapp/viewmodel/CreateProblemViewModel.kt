package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.psdmclientapp.model.request.SessionRequest
import com.example.psdmclientapp.model.SessionResponse
import com.example.psdmclientapp.model.request.ProblemRequest
import com.example.psdmclientapp.network.ApiClient
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.model.request.CreateProblemAndSessionRequest
import com.example.psdmclientapp.model.DecisionMakingMethodResponse
import com.example.psdmclientapp.model.ProblemSolvingMethodResponse
import kotlinx.coroutines.launch
import kotlin.Long

@HiltViewModel
class CreateProblemViewModel @Inject constructor() : ViewModel() {

    var title by mutableStateOf("")
    var description by mutableStateOf("")

    var selectedSolvingMethodId by mutableStateOf<Long?>(null)
    var selectedDecisionMethodId by mutableStateOf<Long?>(null)

    var problemSolvingMethods by mutableStateOf<List<ProblemSolvingMethodResponse>>(emptyList())
    var decisionMakingMethods by mutableStateOf<List<DecisionMakingMethodResponse>>(emptyList())

    val attributeTitles = mutableStateListOf<String>()

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        fetchMethods()
    }

    private fun fetchMethods() {
        viewModelScope.launch {
            try {
                problemSolvingMethods = ApiClient.methodApi.getSolvingSolvingMethods()
                decisionMakingMethods = ApiClient.methodApi.getDecisionMakingMethods()
            } catch (e: Exception) {
                errorMessage = "Failed to load methods: ${e.message}"
            }
        }
    }



    suspend fun submit(onSuccess: (SessionResponse) -> Unit) {
        try {
            isLoading = true
            errorMessage = null

            val session = ApiClient.sessionApi.createProblemAndSession(
                CreateProblemAndSessionRequest(
                    ProblemRequest(
                        title = title,
                        description = description,
                        moderatorId = 1L,
                    ),
                    SessionRequest(
                        problemId = 1,
                        problemSolvingMethodId = selectedSolvingMethodId!!,
                        decisionMakingMethodId = selectedDecisionMethodId!!
                    )
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
