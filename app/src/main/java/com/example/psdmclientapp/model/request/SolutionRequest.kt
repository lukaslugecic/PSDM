package com.example.psdmclientapp.model.request

data class SolutionRequest (
    val title: String,
    val description: String,
    val userId: Long?,
    val problemId: Long,
    val sessionId: Long
)