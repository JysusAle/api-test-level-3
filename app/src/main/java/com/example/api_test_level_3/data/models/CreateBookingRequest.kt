package com.example.api_test_level_3.data.models

data class CreateBookingRequest(
    val barbershopId: String,
    val barberId: String,
    val serviceId: String,
    val userId: String,
    val date: String,
    val timeSlot: String,
    val notes: String?
)

//Creacion del modelo para eñ response:


data class CreateBookingResponse(
    val success: Boolean,
    val message: String,
    val data: BookingData?
)

data class BookingData(
    val bookingId: String,
    val barbershopId: String,
    val barberId: String,
    val serviceId: String,
    val serviceName: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val totalPrice: Double,
    val status: String,
    val paymentStatus: String,
    val createdAt: String
)