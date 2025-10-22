package com.example.api_test_level_3.data.network

import com.example.api_test_level_3.data.network.ApiResponse
import com.example.api_test_level_3.data.models.CreateBookingRequest
import com.example.api_test_level_3.data.models.CreateBookingResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("prod/v1/cognito/get-user-bookings")
    fun getUserBookings(): Call<ApiResponse>
    
    @POST("prod/v1/cognito/create-booking")
    fun createBooking(@Body request: CreateBookingRequest): Call<CreateBookingResponse>
}