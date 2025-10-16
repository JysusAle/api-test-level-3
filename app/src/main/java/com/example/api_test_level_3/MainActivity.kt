package com.example.api_test_level_3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.api_test_level_3.data.network.ApiResponse
import com.example.api_test_level_3.data.network.Booking
import com.example.api_test_level_3.data.network.RetrofitClient
import com.example.api_test_level_3.ui.theme.Apitestlevel3Theme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.example.api_test_level_3.data.models.CreateBookingRequest
import com.example.api_test_level_3.data.models.CreateBookingResponse
class MainActivity : ComponentActivity() {
    private val TAG = "MiAPI"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Apitestlevel3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun testApiCall(onResult: (String) -> Unit) {
        val call: Call<ApiResponse> = RetrofitClient.apiService.getUserBookings()
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!

                    // Convertimos los datos a un String legible
                    val displayText = buildString {
                        append("Success: ${apiResponse.success}\n")
                        append("bookings: ${apiResponse.data.bookings}\n")
                        append("Timestamp: ${apiResponse.timestamp}\n\n")
                        append("Bookings:\n")
                        apiResponse.data.bookings.forEach { booking ->
                            append(formatBooking(booking))
                            append("\n-------------------\n")
                        }
                        append("Total Bookings: ${apiResponse.data.bookings.size}")
                    }

                    Log.d(TAG, displayText)
                    onResult(displayText)
                } else {
                    Log.e(TAG, "Error HTTP: ${response.code()}")
                    onResult("Error HTTP: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e(TAG, "Falla en la petición: ${t.message}", t)
                onResult("Falla en la petición: ${t.message}")
            }
        })
    }

    private fun formatBooking(booking: Booking): String {
        return buildString {
            append("Booking ID: ${booking.bookingId}\n")
            append("User ID: ${booking.userId}\n")
            append("Barbershop: ${booking.barbershop?.name ?: "N/A"}\n")
            append("Service: ${booking.service?.serviceName ?: "N/A"}\n")
            append("Barber: ${booking.barber?.barberName ?: "N/A"}\n")
            append("Date: ${booking.bookingDate}\n")
            append("Start: ${booking.startTime}, End: ${booking.endTime}\n")
            append("Status: ${booking.status}, Payment: ${booking.paymentStatus}\n")
            append("Total Price: ${booking.totalPrice ?: 0.0}")
        }
    }

    @Composable
    fun MainScreen(modifier: Modifier = Modifier) {
        var loading by remember { mutableStateOf(false) }
        var resultText by remember { mutableStateOf("") }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    loading = true
                    testApiCall { result ->
                        resultText = result
                        loading = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (loading) "Cargando..." else "GET BOOKINGS")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = resultText)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    loading = true
                    testCreateBooking { result ->
                        resultText = result
                        loading = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (loading) "Creando..." else "CREATE BOOKING")
            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        Apitestlevel3Theme {
            MainScreen()
        }
    }

    private fun testCreateBooking(onResult: (String) -> Unit) {
        // 1️Construimos el cuerpo del POST (el request)
        val bookingRequest = CreateBookingRequest(
            barbershopId = "f942e69e-d29e-45d6-8800-26bf1947dd1e",
            barberId = "barber-gc001",
            serviceId = "service-003",
            userId = "64180468-0041-70f3-3803-56bd3f154a00",
            date = "2025-10-04",
            timeSlot = "2:00 PM",
            notes = "Booking for tomorrow afternoon"
        )

        // 2️Creamos la llamada Retrofit

        val call = RetrofitClient.apiService.createBooking(bookingRequest)
        call.enqueue(object : Callback<CreateBookingRequest> {
            override fun onResponse(
                call: Call<CreateBookingRequest>,
                response: Response<CreateBookingRequest>
            ) {
                if (response.isSuccessful) {
                    val booking = response.body()
                    Log.d(TAG, "Reserva creada correctamente: $booking")
                } else {
                    Log.e(TAG, "Error al crear reserva: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<CreateBookingRequest>, t: Throwable) {
                Log.e(TAG, "Fallo POST createBooking: ${t.message}", t)
            }
        })

    }

}

