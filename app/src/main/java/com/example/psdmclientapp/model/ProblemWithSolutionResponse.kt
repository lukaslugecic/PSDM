package com.example.psdmclientapp.model

data class ProblemWithSolutionResponse(
    val id: Long,
    val title: String,
    val description: String,
    val solution: SolutionResponse?
)
