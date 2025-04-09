package com.example.psdmclientapp.model

import java.time.LocalDateTime

data class Solution (
    val id: Long,
    val title: String,
    val description: String,
    val userId: Long,
    val problemId: Long,
    val sessionId: Long,
    val createdTime: String,
    val chosen: Boolean
)