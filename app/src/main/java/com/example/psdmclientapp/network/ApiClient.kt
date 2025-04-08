package com.example.psdmclientapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8081/api/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val problemApi: ProblemApiService = retrofit.create(ProblemApiService::class.java)
    val userApi: UserApiService = retrofit.create(UserApiService::class.java)
}

