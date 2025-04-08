package com.example.psdmclientapp.model

data class SessionRequest(
    val problemId: Long,
    val solvingMethod: String,
    val decisionMethod: String
)