package com.example.api_test_level_3.data.network

data class ApiResponse(
    val success: Boolean,
    val data: BookingData,
    val timestamp: String
)

data class BookingData(
    val bookings: List<Booking>,
    val pagination: Pagination
)

data class Booking(
    val barbershopId: String?,
    val serviceId: String?,
    val status: String?,
    val userId: String?,
    val updatedAt: String?,
    val notes: String?,
    val bookingId: String?,
    val createdAt: String?,
    val paymentStatus: String?,
    val barberId: String?,
    val endTime: String?,
    val startTime: String?,
    val totalPrice: Double?,
    val bookingDate: String?,
    val service: Service?,
    val barbershop: Barbershop?,
    val barber: Barber?,
    val user: User?,
    val requestedNewDate: String?,
    val rescheduleReason: String?,
    val requestedNewTime: String?
)

data class Service(
    val serviceId: String?,
    val serviceName: String?,
    val description: String?,
    val price: Double?,
    val duration: String?,
    val category: String?
)

data class Barbershop(
    val barbershopId: String?,
    val name: String?,
    val address: String?,
    val city: String?,
    val phone: String?
)

data class Barber(
    val barberId: String?,
    val barberName: String?,
    val barberEmail: String?
)

data class User(
    val userId: String?,
    val userName: String?,
    val userEmail: String?,
    val userPhone: String?
)

data class Pagination(
    val count: Int?,
    val hasMore: Boolean?
)
