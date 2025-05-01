package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.request.CreateProblemAndSessionRequest
import com.example.psdmclientapp.model.request.InviteUserRequest
import com.example.psdmclientapp.model.SessionDetailsResponse
import com.example.psdmclientapp.model.SessionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SessionApiService {
    @GET("session/details/{id}")
    suspend fun getSessionDetails(@Path("id") sessionId: Long): Response<SessionDetailsResponse>

    @POST("session/andProblem")
    suspend fun createProblemAndSession(@Body request: CreateProblemAndSessionRequest): SessionResponse

    @POST("session/addUsers")
    suspend fun inviteUsers(@Body request: InviteUserRequest)

    @GET("session/{sessionId}/turn")
    suspend fun getCurrentTurnUserId(@Path("sessionId") sessionId: Long): Long

    @POST("session/{sessionId}/turn/next")
    suspend fun advanceTurn(@Path("sessionId") sessionId: Long)
}