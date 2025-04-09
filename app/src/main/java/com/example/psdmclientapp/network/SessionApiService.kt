package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.SessionInfo
import com.example.psdmclientapp.model.SessionRequest
import com.example.psdmclientapp.model.SessionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SessionApiService {
    @GET("api/session/{id}")
    suspend fun getSessionInfo(@Path("id") sessionId: Long): Response<SessionInfo>

    @POST("api/session/{id}/start")
    suspend fun startSession(@Path("id") sessionId: Long): Response<Unit>

    @POST("/api/session")
    suspend fun createSession(@Body request: SessionRequest): SessionResponse
}