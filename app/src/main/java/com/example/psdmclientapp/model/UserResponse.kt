package com.example.psdmclientapp.model

import java.time.LocalDate

data class UserResponse (
    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    val dateOdBirth: LocalDate,
    val roleId: Long
)