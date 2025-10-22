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

    private const val TOKEN = "eyJraWQiOiIwQUdUZTlXemNtTUV1SXR2RXh5RjVSUWhTY0xZblJNTHFoN0w5R2Z3aGM0PSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI2NDE4MDQ2OC0wMDQxLTcwZjMtMzgwMy01NmJkM2YxNTRhMDAiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLWVhc3QtMS5hbWF6b25hd3MuY29tXC91cy1lYXN0LTFfMVlqTnhOQUJRIiwicGhvbmVfbnVtYmVyX3ZlcmlmaWVkIjp0cnVlLCJjb2duaXRvOnVzZXJuYW1lIjoiNTAyYzEwOTQtODAyNS00YzNhLWI0YzktMzAyMzNlZTlmYjNhIiwib3JpZ2luX2p0aSI6IjY2ZmM5MDlmLWZlM2EtNDk0OS1hZTFkLWFhOGVjYWJiNjkzZCIsImF1ZCI6IjUwc3U5NmJibWtyN280NDQ0M2xlZ2NqYWVtIiwiZXZlbnRfaWQiOiI2MDQxMTAxZC1kNTY1LTQ2YjctYjBmOS01ZmJmOTU4NjdhNDkiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTc2MTE2MDYxOSwibmFtZSI6Ik1hcmlvIE1vbGluYSIsInBob25lX251bWJlciI6Iis1MjU1MzIxMTYyNDgiLCJleHAiOjE3NjEyNDcwMTksImlhdCI6MTc2MTE2MDYxOSwianRpIjoiZGY1ODY2OTUtMmU5MC00MjlkLTk0MWEtODk5NjUwM2M2ZTliIiwiZW1haWwiOiJtYXJpby5tb2xpbmFAYXNpbW92Lm14In0.klWIGJQK1C_bibUdysVuFDzvjGv3RpTYa850DfNEyvjYztXTuDEh8yUIkBUIvUUI3Fs5427iA6bKsszlm-9J5cw88HMUpucHJhqQ-rbQGBP8afPIL4LsOAj_Yw56jJyezvTZEBi0sTK6oN0g0alh8wEtRHTi6oO2bkTN7x6DufnIz8wLqJ_9kiN9_QROXLH5Jh-lltKGGuJrArnjRBJesWd9BkSzlSxQYIwi92zQoQQKdMGkOezmBJa4fipN74p5e1k4Xlgi88htPlv7vLRq4Wo43xSWyO-w7ncsDq0jwzKS9lWwlQ6EenoA41E0mt5hubEiNlSLRuoR1-qPCDJHqg"

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
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}