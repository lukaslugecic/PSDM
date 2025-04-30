package com.example.psdmclientapp.model.request

data class InviteUserRequest(
    val sessionId: Long,
    val userIds: List<Long>
)