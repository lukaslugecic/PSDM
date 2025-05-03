package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.SolutionResponse
import com.example.psdmclientapp.model.request.GroupSolutionRequest
import com.example.psdmclientapp.model.request.SolutionRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SolutionApiService {

    @POST("solution")
    suspend fun submitSolution(@Body solutionRequest: SolutionRequest) : SolutionResponse

    @GET("solution/session/{sessionId}")
    suspend fun getSolutionsBySessionId(@Path("sessionId") sessionId: Long) : List<SolutionResponse>

    @GET("solution/session/parent/{sessionId}")
    suspend fun getSolutionsByParentSessionIdOrSessionId(@Path("sessionId") sessionId: Long) : List<SolutionResponse>

    @POST("solution/group")
    suspend fun groupSolutions(@Body groupSolutionRequest: GroupSolutionRequest) : SolutionResponse
}
