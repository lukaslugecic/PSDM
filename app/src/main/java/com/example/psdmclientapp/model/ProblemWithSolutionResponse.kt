package com.example.psdmclientapp.model

data class ProblemWithSolutionResponse(
    val problem: ProblemResponse,
    val solution: SolutionResponse?
)
