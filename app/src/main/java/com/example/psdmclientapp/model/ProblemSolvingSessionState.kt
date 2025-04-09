package com.example.psdmclientapp.model

data class ProblemSolvingSessionState(
    val sessionId: Long? = null,
    val problemTitle: String = "",
    val problemDescription: String = "",
    val solutions: List<Solution> = emptyList(),
    val isOwner: Boolean = false,
    val errorMessage: String? = null
)