package com.example.api_test_level_3.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetrofitClient {

    private const val BASE_URL = "https://wqidlsv4wk.execute-api.us-east-1.amazonaws.com/"

    private const val TOKEN = "eyJraWQiOiIwQUdUZTlXemNtTUV1SXR2RXh5RjVSUWhTY0xZblJNTHFoN0w5R2Z3aGM0PSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI2NDE4MDQ2OC0wMDQxLTcwZjMtMzgwMy01NmJkM2YxNTRhMDAiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLWVhc3QtMS5hbWF6b25hd3MuY29tXC91cy1lYXN0LTFfMVlqTnhOQUJRIiwicGhvbmVfbnVtYmVyX3ZlcmlmaWVkIjp0cnVlLCJjb2duaXRvOnVzZXJuYW1lIjoiNTAyYzEwOTQtODAyNS00YzNhLWI0YzktMzAyMzNlZTlmYjNhIiwib3JpZ2luX2p0aSI6IjJiMWYyOGNmLTE0N2MtNDA5Yi04OGJhLTI2NjJlODI5YmViZSIsImF1ZCI6IjUwc3U5NmJibWtyN280NDQ0M2xlZ2NqYWVtIiwiZXZlbnRfaWQiOiI1NjgxOWRmNi04NjdlLTRjYmEtYjRlMS1iMDA1MWNlNGQ1MzIiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTc2MDU2MjMwMCwibmFtZSI6Ik1hcmlvIE1vbGluYSIsInBob25lX251bWJlciI6Iis1MjU1MzIxMTYyNDgiLCJleHAiOjE3NjA2NDg3MDAsImlhdCI6MTc2MDU2MjMwMSwianRpIjoiZmI0Mzc5MWQtNzRlOC00OWVmLWIwMTEtNDIzYzQzNDU4NDQzIiwiZW1haWwiOiJtYXJpby5tb2xpbmFAYXNpbW92Lm14In0.ERG5wHbl6T-rNsFWyzcm63zpYtstuYvsLM7LdhZRysMcisNgPmTc4KjsT3JdrunwIbW5m4zTE11FUNKUcjzqh8GHMzzMOd0yzCMYdBzYuKvgyYRQZR6EEDRG5G7jjLdY0KWH9EyjAZxIrQNfK0i9mHJTIo8ysJYVg-PcoRijU5ApIlLU695iANLrRaI_DmF7uyz7hzfLiRqoQFagmuF3ce8Iv1FhimhuQ4qTRbCXcWAMSQuWCpRq9iHvvZ15YQeXDxXEFHvY_28DoqlonLeBS0jV8RINpMmaa5m7DAnCl8ijYh3o-ZVJlH_epLJaq_5DD0yH6A431YwAwQxDlTs7gw"

    private val headerInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $TOKEN")
            .build()
        chain.proceed(request)
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)  // agregamos headers
        .addInterceptor(logging)            // logging opcional
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)

            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)

        .addConverterFactory(ScalarsConverterFactory.create()).build() }


//Configuracion general para hacer las peticiones