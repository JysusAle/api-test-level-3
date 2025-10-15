package com.example.api_test_level_3.data.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    @GET("prod/v1/cognito/get-user-bookings")
    fun getUserBookings(): Call<ResponseBody>
}