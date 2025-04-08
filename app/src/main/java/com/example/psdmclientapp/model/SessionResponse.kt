package com.example.psdmclientapp.model

data class SessionResponse(
    val id: Long,
    val problemId: Long,
    val solvingMethod: String,
    val decisionMethod: String
)