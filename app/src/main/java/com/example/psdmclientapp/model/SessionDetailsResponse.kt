package com.example.psdmclientapp.model

data class SessionDetailsResponse (
    val id: Long,
    val problemTitle: String,
    val start: String,
    val end: String,
    val problemSolvingMethod: String,
    val decisionMakingMethod: String,
    val attributes: List<String>
)