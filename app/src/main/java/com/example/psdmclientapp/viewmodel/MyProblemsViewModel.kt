package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.model.ProblemWithSolutionResponse
import com.example.psdmclientapp.network.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyProblemsViewModel @Inject constructor() : ViewModel() {
    var problemDetails by mutableStateOf<List<ProblemWithSolutionResponse>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        fetchProblems()
    }

    private fun fetchProblems() {
        viewModelScope.launch {
            try {
                isLoading = true
                problemDetails = ApiClient.problemApi.getUserProblemsWithSolutions(1)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
            } finally {
                isLoading = false
            }
        }
    }
}
