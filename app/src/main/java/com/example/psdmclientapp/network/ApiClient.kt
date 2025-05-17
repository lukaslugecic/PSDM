package com.example.psdmclientapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://192.168.186.220:8081/api/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userApi: UserApiService = retrofit.create(UserApiService::class.java)
    val sessionApi: SessionApiService = retrofit.create(SessionApiService::class.java)
    val solutionApi: SolutionApiService = retrofit.create(SolutionApiService::class.java)
    val methodApi: MethodApiService = retrofit.create(MethodApiService::class.java)
    val voteApi: VoteApiService = retrofit.create(VoteApiService::class.java)
    val problemApi: ProblemApiService = retrofit.create(ProblemApiService::class.java)

}

