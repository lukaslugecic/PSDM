package com.example.psdmclientapp.model.request

data class SessionRequest(
    val problemId: Long,
    val problemSolvingMethodId: Long,
    val decisionMakingMethodId: Long,
    val duration: Long,
    val attributes: String
)