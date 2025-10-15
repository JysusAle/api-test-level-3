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

    private const val TOKEN = "eyJraWQiOiIwQUdUZTlXemNtTUV1SXR2RXh5RjVSUWhTY0xZblJNTHFoN0w5R2Z3aGM0PSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI2NDE4MDQ2OC0wMDQxLTcwZjMtMzgwMy01NmJkM2YxNTRhMDAiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLWVhc3QtMS5hbWF6b25hd3MuY29tXC91cy1lYXN0LTFfMVlqTnhOQUJRIiwicGhvbmVfbnVtYmVyX3ZlcmlmaWVkIjp0cnVlLCJjb2duaXRvOnVzZXJuYW1lIjoiNTAyYzEwOTQtODAyNS00YzNhLWI0YzktMzAyMzNlZTlmYjNhIiwib3JpZ2luX2p0aSI6IjU2ZjMwZDY2LTZkZmQtNDEwNy04MzVlLTc5ZGIwOTkwZGFhNyIsImF1ZCI6IjUwc3U5NmJibWtyN280NDQ0M2xlZ2NqYWVtIiwiZXZlbnRfaWQiOiJlMjgwMWU3YS1kOWZkLTRlYjktYWEzMC0wYTllOGVlMGU4ZGEiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTc2MDQ3NDU0NCwibmFtZSI6Ik1hcmlvIE1vbGluYSIsInBob25lX251bWJlciI6Iis1MjU1MzIxMTYyNDgiLCJleHAiOjE3NjA1NjA5NDQsImlhdCI6MTc2MDQ3NDU0NCwianRpIjoiOGZlNWM4OGEtYjE0MC00ODk1LWJmZWQtNjg5YmUyNzAwNzE1IiwiZW1haWwiOiJtYXJpby5tb2xpbmFAYXNpbW92Lm14In0.ovl3ZWSyFAe1kmy7SjLO7Amqk77y9iDsEPh_V09gQAMLh5kM9dyZEtTKUTZ9K4ib8W56SHEmx8d2gZTexmrRFSTFiiAvbGLUvrPbsgGTC9ZLRt-0cTIJTCSrmH8svRlSLh0fN41hhYV5STMtYoMGqeo38ksZSo24JHGcbcM6IQqYEFODeHEY3SEHaadfg0d8iKPjxo_3ZN7a5J04yQnXeB_myBFkubS4h6Ey0fR1KfcSqVuwATuKt3IYjd689sPpkUe6HBhA93_deB7vzcCltkh82qHZu06w3EPigophaiqn9gJ23A_3mEpQU1u9Ou-9tLkuBUaPPlKz_p3zYn5fOg"

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

    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)

        .addConverterFactory(ScalarsConverterFactory.create()).build() }


//Configuracion general para hacer las peticiones