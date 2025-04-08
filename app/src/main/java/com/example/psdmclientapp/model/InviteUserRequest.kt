package com.example.psdmclientapp.model

data class InviteUserRequest(
    val sessionId: Long,
    val userIds: List<Long>
)
