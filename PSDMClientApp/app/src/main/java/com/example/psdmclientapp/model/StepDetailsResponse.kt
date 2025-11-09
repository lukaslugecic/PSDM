package com.example.psdmclientapp.model

data class StepDetailsResponse (
    val id: Long,
    val title: String,
    val description: String,
    val ordinal: Integer,
    val duration: String
)