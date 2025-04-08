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

@HiltViewModel
class SolveProblemViewModel @Inject constructor() : ViewModel() {

    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var selectedSolvingMethod by mutableStateOf("")
    var selectedDecisionMethod by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    suspend fun submit(onSuccess: (SessionResponse) -> Unit) {
        try {
            isLoading = true
            errorMessage = null

            val problem = ApiClient.problemApi.createProblem(
                ProblemRequest(title, description)
            )

            val session = ApiClient.problemApi.createSession(
                SessionRequest(
                    problemId = problem.id,
                    solvingMethod = selectedSolvingMethod,
                    decisionMethod = selectedDecisionMethod
                )
            )

            onSuccess(session)

        } catch (e: Exception) {
            errorMessage = e.localizedMessage
        } finally {
            isLoading = false
        }
    }
}
