package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.model.SolutionResponse
import com.example.psdmclientapp.network.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DecisionResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val sessionId : Long = checkNotNull(savedStateHandle["sessionId"])

    var winningSolution by mutableStateOf<SolutionResponse?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun determineWinner() {
        viewModelScope.launch {
            try {
                val winner = ApiClient.solutionApi.getWinningSolution(sessionId)
                winningSolution = winner
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }
}
