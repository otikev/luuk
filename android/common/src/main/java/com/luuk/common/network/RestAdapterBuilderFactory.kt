package com.luuk.common.network

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.luuk.common.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object RestAdapterBuilderFactory {
    lateinit var APP_ENDPOINT: String

    fun unauthenticated(): Retrofit.Builder {
        return builder.client(okClient).baseUrl(
            APP_ENDPOINT
        )
    }

    fun externalEndPoint(externalBaseUrl: String?): Retrofit.Builder {
        return builder.client(okClient).baseUrl(externalBaseUrl!!)
    }

    fun authenticated(authKey: String): Retrofit.Builder {
        return com.luuk.common.network.RestAdapterBuilderFactory.builder.client(
            RestAdapterBuilderFactory.getOkClient(
                authKey
            )
        ).baseUrl(APP_ENDPOINT)
    }

    fun getFileAdapter(absoluteUrl: String?): Retrofit.Builder {
        return builder.baseUrl(absoluteUrl!!)
    }

    private val okClient: OkHttpClient
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val builder = OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(com.luuk.common.network.interceptors.ConnectivityInterceptor())
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(loggingInterceptor)
            }
            return builder.build()
        }

    private fun getOkClient(authKey: String): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(com.luuk.common.network.interceptors.ConnectivityInterceptor())
            .addInterceptor(
                com.luuk.common.network.interceptors.AuthorizationRequestInterceptor(
                    authKey
                )
            )
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    private val builder: Retrofit.Builder
        get() {
            val builder = Retrofit.Builder()
            val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date::class.java, com.luuk.common.network.DateDeserializer())
                .create()
            builder.addConverterFactory(GsonConverterFactory.create(gson))
            return builder
        }
    private val plainStringBuilder: Retrofit.Builder
        get() {
            val builder = Retrofit.Builder()
            builder.addConverterFactory(ScalarsConverterFactory.create())
            return builder
        }
}