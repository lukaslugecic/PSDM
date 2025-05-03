package com.example.psdmclientapp.model.request

data class ProblemRequest(
    val title: String,
    val description: String,
    val moderatorId: Long
)