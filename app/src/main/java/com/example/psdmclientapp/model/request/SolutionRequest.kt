package com.example.psdmclientapp.model.request

data class SolutionRequest (
    val title: String,
    val userId: Long?,
    val problemId: Long,
    val sessionId: Long,
    val attributes: List<AttributeRequest> = emptyList()
)