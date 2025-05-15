package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.ProblemWithSolutionResponse
import retrofit2.http.GET

interface ProblemApiService {
    @GET("problems/with-solutions")
    suspend fun getUserProblemsWithSolutions(): List<ProblemWithSolutionResponse>
}
