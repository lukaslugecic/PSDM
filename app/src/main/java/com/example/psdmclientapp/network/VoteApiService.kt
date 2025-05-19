package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.request.VoteRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface VoteApiService {
    @POST("votes")
    suspend fun submitVotes(@Body votes: List<VoteRequest>)

    @GET("votes/hasEveryoneVoted/{sessionId}")
    suspend fun haveAllUsersVoted(@Path("sessionId") sessionId: Long): Boolean
}