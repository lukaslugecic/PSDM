package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.InviteUserRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
    @POST("api/sessions/invite")
    suspend fun inviteUsers(@Body request: InviteUserRequest)
}