package com.example.psdmclientapp.model

data class ProblemRequest(
    val title: String,
    val description: String,
    val moderatorId: Long,
    val start: String,
    val end: String
)