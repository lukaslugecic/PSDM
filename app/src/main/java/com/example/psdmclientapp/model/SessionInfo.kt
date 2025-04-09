package com.example.psdmclientapp.model

data class SessionInfo(
    val id: Long,
    val problemTitle: String,
    val solvingMethod: String,
    val decisionMethod: String,
    val isOwner: Boolean,
    val participants: List<Participant>
)