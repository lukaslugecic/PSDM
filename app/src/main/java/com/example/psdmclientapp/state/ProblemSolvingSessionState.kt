package com.example.psdmclientapp.state

import com.example.psdmclientapp.enum.ProblemSolvingMethod
import com.example.psdmclientapp.enum.DecisionMakingMethod
import com.example.psdmclientapp.model.SolutionResponse

data class ProblemSolvingSessionState(
    val problemTitle: String = "",
    val problemDescription: String = "",
    val solutions: List<SolutionResponse> = emptyList(),
    val isOwner: Boolean = false,
    val isSubSession : Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUserId: Long? = null,
    val isCurrentTurn: Boolean = false,
    val problemSolvingMethod: ProblemSolvingMethod = ProblemSolvingMethod.BRAINWRITING,
    val decisionMakingMethod: DecisionMakingMethod = DecisionMakingMethod.AVERAGE_WINNER
    )
