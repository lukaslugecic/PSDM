package com.example.psdmclientapp.state

import com.example.psdmclientapp.enum.DecisionMakingMethod
import com.example.psdmclientapp.model.SolutionResponse

data class VotingState(
    val decisionMethod: DecisionMakingMethod = DecisionMakingMethod.MAJORITY_RULE,
    val solutions: List<SolutionResponse> = emptyList(),
    val selectedSolutionId: Long? = null,               // For Majority
    val ratings: Map<Long, Int> = emptyMap(),           // For Average Winner
    val ranking: List<Long> = emptyList(),              // For Borda
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
