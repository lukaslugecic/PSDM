package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.SolutionRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface SolutionApiService {

    @POST("solution/session/{sessionId}")
    suspend fun submitSolution(@Path("sessionId") sessionId: Long, @Body solutionRequest: SolutionRequest)

}
