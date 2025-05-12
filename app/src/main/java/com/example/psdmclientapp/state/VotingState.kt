package com.example.psdmclientapp.state

import com.example.psdmclientapp.enum.DecisionMakingMethod
import com.example.psdmclientapp.model.SolutionResponse

data class VotingState(
    val currentUserId: Long? = null,
    val decisionMethod: DecisionMakingMethod = DecisionMakingMethod.MAJORITY_RULE,
    val solutions: List<SolutionResponse> = emptyList(),
    val ratings: Map<Long, Int> = emptyMap(),
    val errorMessage: String? = null
)
