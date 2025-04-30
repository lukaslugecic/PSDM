package com.example.psdmclientapp.state

import com.example.psdmclientapp.model.Solution

data class ProblemSolvingSessionState(
    val problemTitle: String = "",
    val problemDescription: String = "",
    val solutions: List<Solution> = emptyList(),
    val isOwner: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUserId: Long = 0L // 🔥 New: to know if a solution is "mine"
)
