package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.DecisionMakingMethodDTO
import com.example.psdmclientapp.model.ProblemSolvingMethodDTO
import retrofit2.http.GET

interface MethodApiService {
    @GET("decisionMakingMethod")
    suspend fun getDecisionMakingMethods(): List<DecisionMakingMethodDTO>

    @GET("problemSolvingMethod")
    suspend fun getSolvingSolvingMethods(): List<ProblemSolvingMethodDTO>
}