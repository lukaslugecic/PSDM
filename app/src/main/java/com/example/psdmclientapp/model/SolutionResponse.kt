package com.example.psdmclientapp.model

data class SolutionResponse (
    val id: Long,
    val title: String,
    val userId: Long,
    val problemId: Long,
    val sessionId: Long,
    val createdTime: String,
    val chosen: Boolean,
    val grouped: Boolean,
    val attributes: List<AttributeResponse> = emptyList()
)