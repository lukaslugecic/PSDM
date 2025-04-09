package com.example.psdmclientapp.model

data class DecisionPhaseState(
    val sessionId: Long? = null,
    val decisionMethod: String = "Average Winner",
    val ideas: List<Solution> = emptyList(),
    val ratings: Map<Long, Int> = emptyMap(),
    val errorMessage: String? = null
)
