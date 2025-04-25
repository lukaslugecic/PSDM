package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.InviteUserRequest
import com.example.psdmclientapp.model.SessionInfo
import com.example.psdmclientapp.model.SessionRequest
import com.example.psdmclientapp.model.SessionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SessionApiService {
    @GET("session/{id}")
    suspend fun getSessionInfo(@Path("id") sessionId: Long): Response<SessionInfo>

    @POST("session/{id}/start")
    suspend fun startSession(@Path("id") sessionId: Long): Response<Unit>

    @POST("session")
    suspend fun createSession(@Body request: SessionRequest): SessionResponse

    @POST("session/addUsers")
    suspend fun inviteUsers(@Body request: InviteUserRequest)
}