package com.example.psdmclientapp.network

import android.content.Context
import android.net.Uri
import com.example.psdmclientapp.auth.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthorizationService
import net.openid.appauth.connectivity.ConnectionBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://192.168.186.220:8081/api/"

    private val allowHttpConnectionBuilder = ConnectionBuilder { uri: Uri ->
        (URL(uri.toString()).openConnection() as HttpURLConnection).apply {
            connectTimeout = 10_000
            readTimeout    = 10_000
        }
    }

    @Provides
    @Singleton
    fun provideAppAuthConfig(): AppAuthConfiguration =
        AppAuthConfiguration.Builder()
            .setConnectionBuilder(allowHttpConnectionBuilder)
            .build()

    @Provides
    @Singleton
    fun provideAuthService(
        @ApplicationContext ctx: Context,
        config: AppAuthConfiguration
    ): AuthorizationService =
        AuthorizationService(ctx, config)

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        @ApplicationContext ctx: Context
    ): AuthInterceptor =
        AuthInterceptor(ctx)

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        @ApplicationContext ctx: Context,
        authService: AuthorizationService
    ): TokenAuthenticator =
        TokenAuthenticator(ctx, authService)



    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides @Singleton
    fun provideSessionApi(retrofit: Retrofit): SessionApiService =
        retrofit.create(SessionApiService::class.java)

    @Provides @Singleton
    fun provideSolutionApi(retrofit: Retrofit): SolutionApiService =
        retrofit.create(SolutionApiService::class.java)

    @Provides @Singleton
    fun provideMethodApi(retrofit: Retrofit): MethodApiService =
        retrofit.create(MethodApiService::class.java)

    @Provides @Singleton
    fun provideVoteApi(retrofit: Retrofit): VoteApiService =
        retrofit.create(VoteApiService::class.java)

    @Provides @Singleton
    fun provideProblemApi(retrofit: Retrofit): ProblemApiService =
        retrofit.create(ProblemApiService::class.java)
}
