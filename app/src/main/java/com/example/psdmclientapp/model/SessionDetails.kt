package com.example.psdmclientapp.model

import java.time.LocalDateTime

data class SessionDetails(
    val problem: ProblemResponse,
    val session: SessionResponse
)