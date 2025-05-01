package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.DecisionMakingMethodResponse
import com.example.psdmclientapp.model.ProblemSolvingMethodResponse
import retrofit2.http.GET

interface MethodApiService {
    @GET("decisionMakingMethod")
    suspend fun getDecisionMakingMethods(): List<DecisionMakingMethodResponse>

    @GET("problemSolvingMethod")
    suspend fun getSolvingSolvingMethods(): List<ProblemSolvingMethodResponse>
}