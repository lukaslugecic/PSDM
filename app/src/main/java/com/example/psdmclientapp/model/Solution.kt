package com.example.psdmclientapp.model

import java.time.LocalDateTime

data class Solution (
    val id: Long? = null,
    val title: String,
    val description: String,
    val userId: Long,
    val problemId: Long,
    val sessionId: Long,
    val createdTime: LocalDateTime,
    val chosen: Boolean
)