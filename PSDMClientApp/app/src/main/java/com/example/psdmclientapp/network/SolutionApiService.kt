package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.SolutionDetailResponse
import com.example.psdmclientapp.model.SolutionResponse
import com.example.psdmclientapp.model.SolutionScoreResponse
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

    @GET("solution/view/session/{sessionId}")
    suspend fun getSolutionDetailsBySessionId(@Path("sessionId") sessionId: Long) : List<SolutionDetailResponse>

    @POST("solution/group")
    suspend fun groupSolutions(@Body groupSolutionRequest: GroupSolutionRequest) : SolutionResponse

    @GET("solution/bestSolutions/{sessionId}")
    suspend fun getBestSolutions(@Path("sessionId") sessionId: Long): List<SolutionScoreResponse>
}
