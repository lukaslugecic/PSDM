package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.Solution
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SolutionApiService {

    // POST nova ideja
    @POST("solutions")
    suspend fun submitIdea(@Body solution: Solution): Solution

    // GET sve ideje za neki problem
    @GET("solution/problem/{problemId}")
    suspend fun getSolutionsForProblem(@Path("problemId") problemId: Long): List<Solution>
}
