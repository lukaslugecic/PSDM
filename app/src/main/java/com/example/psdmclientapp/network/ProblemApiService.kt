package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.ProblemResponse
import com.example.psdmclientapp.model.ProblemWithSolutionResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ProblemApiService {
    @GET("problem/{id}")
    suspend fun getProblem(@Path("id") problemId: Long) : ProblemResponse

    @GET("problem/with-solutions/user/{id}")
    suspend fun getUserProblemsWithSolutions(@Path("id") userId: Long): List<ProblemWithSolutionResponse>
}
