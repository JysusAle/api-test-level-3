package com.example.api_test_level_3.data.network

import retrofit2.Call
import retrofit2.http.GET
import com.example.api_test_level_3.data.models.CreateBookingRequest
import com.example.api_test_level_3.data.models.CreateBookingResponse
import retrofit2.http.POST
import retrofit2.http.Body


interface ApiService {
    @GET("prod/v1/cognito/get-user-bookings")
    fun getUserBookings(): Call<ApiResponse>

    //Post que envia el request model y querecibe la respuesta en crudo
    @POST("prod/v1/cognito/create-booking")
    fun createBooking(
        @Body bookingRequest: CreateBookingRequest
    ): Call<CreateBookingResponse>
}