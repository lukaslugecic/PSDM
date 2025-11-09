package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.model.SolutionDetailResponse
import com.example.psdmclientapp.network.SolutionApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionSolutionsViewModel @Inject constructor(
    private val solutionApi: SolutionApiService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val sessionId: Long = savedStateHandle.get<Long>("sessionId")
        ?: throw IllegalArgumentException("Session ID missing in SavedStateHandle")

    var solutions by mutableStateOf<List<SolutionDetailResponse>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchSolutions()
    }

    private fun fetchSolutions() {
        viewModelScope.launch {
            try {
                isLoading = true
                solutions = solutionApi.getSolutionDetailsBySessionId(sessionId)

            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
            } finally {
                isLoading = false
            }
        }
    }
}