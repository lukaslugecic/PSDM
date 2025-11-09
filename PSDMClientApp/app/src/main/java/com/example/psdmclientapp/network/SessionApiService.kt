package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.request.CreateProblemAndSessionRequest
import com.example.psdmclientapp.model.request.InviteUserRequest
import com.example.psdmclientapp.model.ProblemSessionResponse
import com.example.psdmclientapp.model.SessionDetailsResponse
import com.example.psdmclientapp.model.SessionResponse
import com.example.psdmclientapp.model.request.SessionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SessionApiService {
    @GET("session/details/{id}")
    suspend fun getSessionDetails(@Path("id") sessionId: Long): Response<ProblemSessionResponse>

    @POST("session/andProblem")
    suspend fun createProblemAndSession(@Body request: CreateProblemAndSessionRequest): SessionResponse

    @POST("session")
    suspend fun createSession(@Body request: SessionRequest): SessionResponse

    @POST("session/addUsers")
    suspend fun inviteUsers(@Body request: InviteUserRequest)

    @GET("session/view/problem/{problemId}")
    suspend fun getSessionsForProblem(@Path("problemId") problemId: Long) : List<SessionDetailsResponse>
}