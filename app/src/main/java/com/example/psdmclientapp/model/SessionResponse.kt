package com.example.psdmclientapp.model

data class SessionResponse(
    val id: Long,
    val problemId: Long,
    val problemSolvingMethodId: Long,
    val decisionMakingMethodId: Long,
    val start: String,
    val end: String
)