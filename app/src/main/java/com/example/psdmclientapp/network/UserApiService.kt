package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiService {
    @GET("user")
    suspend fun getUsers(): List<UserResponse>

    @GET("user/currentSubSession/{userId}")
    suspend fun getCurrentSubSessionId(@Path("userId") userId: Long) : Long

    @GET("user/checkParentSession/{userId}")
    suspend fun checkParentSession(@Path("userId") userId: Long) : Boolean

}