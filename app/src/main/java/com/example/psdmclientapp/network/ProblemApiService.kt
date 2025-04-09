package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.ProblemRequest
import com.example.psdmclientapp.model.ProblemResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ProblemApiService {

    @POST("/api/problem")
    suspend fun createProblem(@Body request: ProblemRequest): ProblemResponse
}
