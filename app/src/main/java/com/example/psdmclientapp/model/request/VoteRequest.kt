package com.example.psdmclientapp.model.request

data class VoteRequest (
    val userId: Long,
    val solutionId: Long,
    val value: Double
)