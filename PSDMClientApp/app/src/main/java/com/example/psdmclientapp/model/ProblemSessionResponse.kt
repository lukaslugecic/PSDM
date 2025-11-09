package com.example.psdmclientapp.model

data class ProblemSessionResponse(
    val problem: ProblemResponse,
    val session: SessionResponse,
    val parentSessionId: Long?,
)