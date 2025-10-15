package com.example.api_test_level_3.data.network

import com.example.api_test_level_3.data.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    // Endpoint relativo (usaremos la baseUrl para no poner @Url)
    @GET("prod/v1/cognito/get-user-bookings")
    fun getUserBookings(): Call<ApiResponse>

    //funciones que se necesitan
}