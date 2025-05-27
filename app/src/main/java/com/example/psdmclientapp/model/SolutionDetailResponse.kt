package com.example.psdmclientapp.model

data class SolutionDetailResponse (
    val id: Long,
    val title: String,
    val firstName: String,
    val lastName: String,
    val attributes: List<AttributeResponse> = emptyList()
)