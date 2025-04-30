package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.request.CreateProblemAndSessionRequest
import com.example.psdmclientapp.model.request.InviteUserRequest
import com.example.psdmclientapp.model.SessionDetails
import com.example.psdmclientapp.model.request.SessionRequest
import com.example.psdmclientapp.model.SessionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SessionApiService {
    @GET("session/details/{id}")
    suspend fun getSessionDetails(@Path("id") sessionId: Long): Response<SessionDetails>

    @POST("session")
    suspend fun createSession(@Body request: SessionRequest): SessionResponse

    @POST("session/andProblem")
    suspend fun createProblemAndSession(@Body request: CreateProblemAndSessionRequest): SessionResponse

    @POST("session/addUsers")
    suspend fun inviteUsers(@Body request: InviteUserRequest)
}