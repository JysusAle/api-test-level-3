package com.example.api_test_level_3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.api_test_level_3.data.models.CreateBookingRequest
import com.example.api_test_level_3.data.models.CreateBookingResponse
import com.example.api_test_level_3.data.network.ApiResponse
import com.example.api_test_level_3.data.network.Booking
import com.example.api_test_level_3.data.network.RetrofitClient
import com.example.api_test_level_3.ui.theme.Apitestlevel3Theme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    // 🔹 Llamada GET
    private fun getBookings(onResult: (List<Booking>) -> Unit, onError: (String) -> Unit) {
        val call: Call<ApiResponse> = RetrofitClient.apiService.getUserBookings()
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val bookings = response.body()!!.data.bookings
                    onResult(bookings)
                } else {
                    onError("Error HTTP: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                onError("Falla en la petición: ${t.message}")
            }
        })
    }

    // 🔹 Llamada POST
    private fun testCreateBooking(onResult: (String) -> Unit) {
        val bookingRequest = CreateBookingRequest(
            barbershopId = "f942e69e-d29e-45d6-8800-26bf1947dd1e",
            barberId = "barber-gc001",
            serviceId = "service-003",
            userId = "64180468-0041-70f3-3803-56bd3f154a00",
            date = "2025-10-04",
            timeSlot = "2:00 PM",
            notes = "Booking for tomorrow afternoon"
        )

        val call = RetrofitClient.apiService.createBooking(bookingRequest)
        call.enqueue(object : Callback<CreateBookingResponse> {
            override fun onResponse(
                call: Call<CreateBookingResponse>,
                response: Response<CreateBookingResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    onResult("Reserva creada correctamente: ${body?.message}")
                } else {
                    onResult("Error al crear reserva: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<CreateBookingResponse>, t: Throwable) {
                onResult("Fallo POST createBooking: ${t.message}")
            }
        })
    }

    // 🧩 Pantalla principal con Jetpack Compose
    @Composable
    fun MainScreen(modifier: Modifier = Modifier) {
        var bookings by remember { mutableStateOf<List<Booking>>(emptyList()) }
        var loading by remember { mutableStateOf(false) }
        var message by remember { mutableStateOf("") }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    loading = true
                    getBookings(
                        onResult = {
                            bookings = it
                            loading = false
                        },
                        onError = {
                            message = it
                            loading = false
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (loading) "Cargando..." else "GET BOOKINGS")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🧱 Lista de reservaciones en cuadritos
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(bookings) { booking ->
                    BookingCard(booking)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    loading = true
                    testCreateBooking { result ->
                        message = result
                        loading = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (loading) "Creando..." else "CREATE BOOKING")
            }

            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    // 🧩 Tarjeta individual (cuadrito)
    @Composable
    fun BookingCard(booking: Booking) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = booking.barbershop?.name ?: "Barbershop desconocida",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(text = "Servicio: ${booking.service?.serviceName ?: "N/A"}")
                Text(text = "Fecha: ${booking.bookingDate ?: "N/A"}")
                Text(text = "Estado: ${booking.status ?: "N/A"}")
                Text(
                    text = "Total: $${booking.totalPrice ?: 0.0}",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewBookingCard() {
        Apitestlevel3Theme {
            BookingCard(
                Booking(
                    barbershopId = "1",
                    serviceId = "1",
                    status = "CONFIRMED",
                    userId = "user-123",
                    updatedAt = null,
                    notes = "Sample note",
                    bookingId = "B001",
                    createdAt = null,
                    paymentStatus = "PAID",
                    barberId = "barber-01",
                    endTime = "4:00 PM",
                    startTime = "3:00 PM",
                    totalPrice = 250.0,
                    bookingDate = "2025-10-19",
                    service = com.example.api_test_level_3.data.network.Service("1", "Corte de cabello", null, 250.0, null, null),
                    barbershop = com.example.api_test_level_3.data.network.Barbershop("1", "Barbería Central", "Av. Reforma", "CDMX", "555-555"),
                    barber = null,
                    user = null,
                    requestedNewDate = null,
                    rescheduleReason = null,
                    requestedNewTime = null
                )
            )
        }
    }
}
