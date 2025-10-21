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

    private const val TOKEN = "eyJraWQiOiIwQUdUZTlXemNtTUV1SXR2RXh5RjVSUWhTY0xZblJNTHFoN0w5R2Z3aGM0PSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI2NDE4MDQ2OC0wMDQxLTcwZjMtMzgwMy01NmJkM2YxNTRhMDAiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLWVhc3QtMS5hbWF6b25hd3MuY29tXC91cy1lYXN0LTFfMVlqTnhOQUJRIiwicGhvbmVfbnVtYmVyX3ZlcmlmaWVkIjp0cnVlLCJjb2duaXRvOnVzZXJuYW1lIjoiNTAyYzEwOTQtODAyNS00YzNhLWI0YzktMzAyMzNlZTlmYjNhIiwib3JpZ2luX2p0aSI6ImFjNWY4ZmIzLTRjMzUtNDQ0MS1iMmY2LWE4Y2IyNGU5YTg4ZSIsImF1ZCI6IjUwc3U5NmJibWtyN280NDQ0M2xlZ2NqYWVtIiwiZXZlbnRfaWQiOiI1MTVmNTZmMC1hMWZjLTQ2MGItOWEzOS1mZjViN2I1MDQ2Y2IiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTc2MDk4ODQ1MiwibmFtZSI6Ik1hcmlvIE1vbGluYSIsInBob25lX251bWJlciI6Iis1MjU1MzIxMTYyNDgiLCJleHAiOjE3NjEwNzQ4NTIsImlhdCI6MTc2MDk4ODQ1MiwianRpIjoiNjgzMzNiMzUtN2FmZi00ZTM2LThhZjQtYTUwNzM1YmFkNzM1IiwiZW1haWwiOiJtYXJpby5tb2xpbmFAYXNpbW92Lm14In0.Ts7yrb6ixqTteUQB5M5XCkyhc_jrfJNgS2_wVSiS7g6iE19OBpij5mdwEFpyOX63pGXs168_WQdRCCOqHAuD2uWstTycucSmU5WNWKnCv0QMlkEdTR4MLonsXSagGiRshjo8h-KsWuYN15505aRt6EPS-pFsUAb4DnEbVEp_9z-2SUCwOXethZErQb97sEyXrqqhdfQZDscIce3jrMWbfr6IFrWAlN0YSWC4jnSeMLZWZ7M7tcFL74Lv55iDWPjDrhw-nUaPrDTLZDlinSSGtZj5qIdWFfoFVcu3UGTUrO0zLlAoI2vo3u7TsGrdS6Z1W8pS3vxvNb1X30t2F1PHXA"
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