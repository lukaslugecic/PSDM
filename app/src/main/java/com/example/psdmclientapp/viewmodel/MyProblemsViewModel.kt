package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.model.AttributeResponse
import com.example.psdmclientapp.model.ProblemWithSolutionResponse
import com.example.psdmclientapp.model.SolutionResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyProblemsViewModel @Inject constructor() : ViewModel() {
    var problems by mutableStateOf<List<ProblemWithSolutionResponse>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        fetchProblems()
    }

    private fun fetchProblems() {
        viewModelScope.launch {
            try {
                isLoading = true
                //problems = ApiClient.problemApi.getUserProblemsWithSolutions()

                problems = listOf(
                    ProblemWithSolutionResponse(
                        id = 1,
                        title = "How to increase team productivity?",
                        description = "Looking for ways to improve workflow efficiency.",
                        solution = SolutionResponse(
                            id = 10,
                            title = "Improve navigation with bottom bar",
                            userId = 3,
                            problemId = 1,
                            sessionId = 101,
                            createdTime = "2025-05-14T12:00:00",
                            chosen = true,
                            grouped = true,
                            attributes = listOf(
                                AttributeResponse(1,1,"Ease of use", "High"),
                                AttributeResponse(1,1, "Impact", "Medium")
                            )
                        )
                    ),
                    ProblemWithSolutionResponse(
                        id = 2,
                        title = "What tech stack for MVP?",
                        description = "Need tech stack suggestion for a small product.",
                        solution = null // No solution yet
                    ),
                    ProblemWithSolutionResponse(
                        id = 2,
                        title = "What tech stack for MVP?",
                        description = "Need tech stack suggestion for a small product.",
                        solution = null // No solution yet
                    ),
                    ProblemWithSolutionResponse(
                        id = 2,
                        title = "What tech stack for MVP?",
                        description = "Need tech stack suggestion for a small product.",
                        solution = null // No solution yet
                    ),
                    ProblemWithSolutionResponse(
                        id = 2,
                        title = "What tech stack for MVP?",
                        description = "Need tech stack suggestion for a small product.",
                        solution = null // No solution yet
                    ),
                    ProblemWithSolutionResponse(
                        id = 2,
                        title = "What tech stack for MVP?",
                        description = "Need tech stack suggestion for a small product.",
                        solution = null // No solution yet
                    ),
                    ProblemWithSolutionResponse(
                        id = 2,
                        title = "What tech stack for MVP?",
                        description = "Need tech stack suggestion for a small product.",
                        solution = null // No solution yet
                    ),
                    ProblemWithSolutionResponse(
                        id = 2,
                        title = "What tech stack for MVP?",
                        description = "Need tech stack suggestion for a small product.",
                        solution = null // No solution yet
                    ),
                    ProblemWithSolutionResponse(
                        id = 2,
                        title = "What tech stack for MVP?",
                        description = "Need tech stack suggestion for a small product.",
                        solution = null // No solution yet
                    ),
                    ProblemWithSolutionResponse(
                        id = 1,
                        title = "How to increase team productivity?",
                        description = "Looking for ways to improve workflow efficiency.",
                        solution = SolutionResponse(
                            id = 10,
                            title = "Improve navigation with bottom bar",
                            userId = 3,
                            problemId = 1,
                            sessionId = 101,
                            createdTime = "2025-05-14T12:00:00",
                            chosen = true,
                            grouped = true,
                            attributes = listOf(
                                AttributeResponse(1,1,"Ease of use", "High"),
                                AttributeResponse(1,1, "Impact", "Medium")
                            )
                        )
                    ),
                )

            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
            } finally {
                isLoading = false
            }
        }
    }
}
