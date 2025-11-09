package com.example.psdmclientapp.model

import java.time.LocalDate

data class UserResponse (
    val id: Long,
    val keycloakId: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val dateOdBirth: LocalDate,
    val roleId: Long
)