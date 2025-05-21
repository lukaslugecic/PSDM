package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.model.SolutionResponse
import com.example.psdmclientapp.model.SolutionScoreResponse
import com.example.psdmclientapp.network.SolutionApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DecisionResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val solutionApi: SolutionApiService
) : ViewModel() {
    private val sessionId : Long = checkNotNull(savedStateHandle["sessionId"])

    var scoredSolutions by mutableStateOf<List<SolutionScoreResponse>>(emptyList())
        private set

    var winningSolution by mutableStateOf<SolutionResponse?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun determineWinner() {
        viewModelScope.launch {
            try {
                val result = solutionApi.getBestSolutions(sessionId)

                scoredSolutions = result

                winningSolution = result.find { it.solution.chosen }?.solution
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }
}
