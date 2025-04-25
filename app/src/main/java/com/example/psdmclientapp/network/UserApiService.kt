package com.example.psdmclientapp.network

import com.example.psdmclientapp.model.User
import retrofit2.http.GET

interface UserApiService {
    @GET("user")
    suspend fun getUsers(): List<User>
}