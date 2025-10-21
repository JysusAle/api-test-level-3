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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.api_test_level_3.data.network.ApiResponse
import com.example.api_test_level_3.data.network.Booking
import com.example.api_test_level_3.data.network.RetrofitClient
import com.example.api_test_level_3.ui.theme.Apitestlevel3Theme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingsActivity : ComponentActivity() {
    private val TAG = "BookingsActivity"

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Apitestlevel3Theme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("Reservas") },
                        )
                    }
                ) { innerPadding ->
                    BookingsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun BookingsScreen(modifier: Modifier = Modifier) {
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
                    fetchBookings(
                        onSuccess = {
                            bookings = it
                            message = "Cargadas ${it.size} reservas."
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
                Text(if (loading) "Cargando..." else "Obtener Reservas")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = message)

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(bookings) { booking ->
                    BookingCard(booking)
                }
            }
        }
    }

    @Composable
    fun BookingCard(booking: Booking) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = booking.barbershop?.name ?: "Sin nombre",
                    fontWeight = FontWeight.Bold
                )
                Text("Servicio: ${booking.service?.serviceName ?: "N/A"}")
                Text("Fecha: ${booking.bookingDate ?: "N/A"}")
                Text("Hora: ${booking.startTime ?: "N/A"} - ${booking.endTime ?: "N/A"}")
                Text("Precio: $${booking.totalPrice ?: 0.0}")
                Text("Estatus: ${booking.status ?: "N/A"}")
            }
        }
    }

    private fun fetchBookings(onSuccess: (List<Booking>) -> Unit, onError: (String) -> Unit) {
        val call: Call<ApiResponse> = RetrofitClient.apiService.getUserBookings()
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    onSuccess(response.body()!!.data.bookings)
                } else {
                    onError("Error HTTP: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e(TAG, "Error al obtener bookings: ${t.message}")
                onError("Falla en la petición: ${t.message}")
            }
        })
    }
}
