package com.example.psdmclientapp.network

import android.content.Context
import com.example.psdmclientapp.auth.TokenStorage
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://192.168.186.220:8081/api/"

    private fun provideOkHttpClient(token: String): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(token))
            .build()
    }

    fun getAuthenticatedRetrofit(context: Context): Retrofit {
        val token = TokenStorage.getToken(context) ?: ""
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient(token))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // If you want a default instance (unauthenticated), keep this:
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getUserApi(context: Context): UserApiService {
        return getAuthenticatedRetrofit(context).create(UserApiService::class.java)
    }

    fun getSessionApi(context: Context): SessionApiService {
        return getAuthenticatedRetrofit(context).create(SessionApiService::class.java)
    }

    fun getSolutionApi(context: Context): SolutionApiService {
        return getAuthenticatedRetrofit(context).create(SolutionApiService::class.java)
    }

    fun getMethodApi(context: Context): MethodApiService {
        return getAuthenticatedRetrofit(context).create(MethodApiService::class.java)
    }

    fun getVoteApi(context: Context): VoteApiService {
        return getAuthenticatedRetrofit(context).create(VoteApiService::class.java)
    }

    fun getProblemApi(context: Context): ProblemApiService {
        return getAuthenticatedRetrofit(context).create(ProblemApiService::class.java)
    }


    val userApi: UserApiService = retrofit.create(UserApiService::class.java)
    val sessionApi: SessionApiService = retrofit.create(SessionApiService::class.java)
    val solutionApi: SolutionApiService = retrofit.create(SolutionApiService::class.java)
    val methodApi: MethodApiService = retrofit.create(MethodApiService::class.java)
    val voteApi: VoteApiService = retrofit.create(VoteApiService::class.java)
    val problemApi: ProblemApiService = retrofit.create(ProblemApiService::class.java)

}

