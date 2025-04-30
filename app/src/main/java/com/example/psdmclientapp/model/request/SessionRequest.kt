package com.example.psdmclientapp.model.request

data class SessionRequest(
    val problemId: Long,
    val problemSolvingMethodId: Long,
    val decisionMakingMethodId: Long,
    val start: String,
    val end: String
)