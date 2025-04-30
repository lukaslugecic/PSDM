package com.example.psdmclientapp.model

data class ProblemResponse(
    val id: Long,
    val title: String,
    val description: String,
    val moderatorId: Long,
    val start: String,
    val end: String
)