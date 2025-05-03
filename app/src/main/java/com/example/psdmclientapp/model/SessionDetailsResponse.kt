package com.example.psdmclientapp.model

data class SessionDetailsResponse(
    val problem: ProblemResponse,
    val session: SessionResponse,
    val steps: List<StepDetailsResponse>
)