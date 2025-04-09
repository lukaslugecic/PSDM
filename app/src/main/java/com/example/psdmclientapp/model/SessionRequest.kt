package com.example.psdmclientapp.model

import java.time.LocalDateTime

data class SessionRequest(
    val problemId: Long,
    val problemSolvingMethodId: Long,
    val decisionMakingMethodId: Long,
    val start: String,
    val end: String
)