package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.ProblemRequest
import com.example.psdmclientapp.model.ProblemResponse
import com.example.psdmclientapp.model.SessionRequest
import com.example.psdmclientapp.model.SessionResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ProblemApiService {

    @POST("/api/problems")
    suspend fun createProblem(@Body request: ProblemRequest): ProblemResponse

    @POST("/api/sessions")
    suspend fun createSession(@Body request: SessionRequest): SessionResponse
}
