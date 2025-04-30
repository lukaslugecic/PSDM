package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.DecisionMakingMethod
import com.example.psdmclientapp.model.ProblemSolvingMethod
import retrofit2.http.GET

interface MethodApiService {
    @GET("decisionMakingMethod")
    suspend fun getDecisionMakingMethods(): List<DecisionMakingMethod>

    @GET("problemSolvingMethod")
    suspend fun getSolvingSolvingMethods(): List<ProblemSolvingMethod>
}