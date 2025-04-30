package com.example.psdmclientapp.model.request

data class CreateProblemAndSessionRequest(
    val problem: ProblemRequest,
    val session: SessionRequest
)